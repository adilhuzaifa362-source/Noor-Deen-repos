package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.data.IslamicData
import com.example.model.ActiveAudioTrack
import com.example.model.AudioPlaybackStatus
import com.example.model.AudioSourceType
import com.example.model.Hadith
import com.example.model.Qari
import com.example.model.Supplication
import com.example.model.Surah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

object AudioPlaybackManager {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var appContext: Context? = null
    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var progressJob: Job? = null

    // Playback States
    private val _playbackStatus = MutableStateFlow(AudioPlaybackStatus.IDLE)
    val playbackStatus: StateFlow<AudioPlaybackStatus> = _playbackStatus.asStateFlow()

    private val _activeTrack = MutableStateFlow<ActiveAudioTrack?>(null)
    val activeTrack: StateFlow<ActiveAudioTrack?> = _activeTrack.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private val _autoAdvanceAyah = MutableStateFlow(true)
    val autoAdvanceAyah: StateFlow<Boolean> = _autoAdvanceAyah.asStateFlow()

    private val _selectedQari = MutableStateFlow(IslamicData.qaris.first())
    val selectedQari: StateFlow<Qari> = _selectedQari.asStateFlow()

    private val _ttsSpeaking = MutableStateFlow(false)
    val ttsSpeaking: StateFlow<Boolean> = _ttsSpeaking.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Listener for ayah advancement so NoorDeenViewModel can sync UI
    var onAyahChangedListener: ((surahNumber: Int, ayahNumber: Int) -> Unit)? = null

    fun initialize(context: Context) {
        if (appContext != null) return
        appContext = context.applicationContext

        initMediaPlayer()
        initTextToSpeech(context.applicationContext)
    }

    private fun initMediaPlayer() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )

                setOnPreparedListener { mp ->
                    _playbackStatus.value = AudioPlaybackStatus.PLAYING
                    _durationMs.value = mp.duration.coerceAtLeast(0)
                    applyPlaybackSpeed()
                    mp.start()
                    startProgressTracker()
                    startForegroundServiceIfAvailable()
                }

                setOnCompletionListener {
                    handlePlaybackCompleted()
                }

                setOnErrorListener { _, what, extra ->
                    _playbackStatus.value = AudioPlaybackStatus.ERROR
                    _errorMessage.value = "Playback error ($what, $extra)"
                    stopProgressTracker()
                    true
                }

                setOnBufferingUpdateListener { _, percent ->
                    // Buffer tracking if needed
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = "Failed to initialize media player: ${e.message}"
        }
    }

    private fun initTextToSpeech(context: Context) {
        try {
            textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady = true
                    textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            scope.launch {
                                _ttsSpeaking.value = true
                                _playbackStatus.value = AudioPlaybackStatus.PLAYING
                            }
                        }

                        override fun onDone(utteranceId: String?) {
                            scope.launch {
                                _ttsSpeaking.value = false
                                _playbackStatus.value = AudioPlaybackStatus.IDLE
                                _activeTrack.value = null
                            }
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            scope.launch {
                                _ttsSpeaking.value = false
                                _playbackStatus.value = AudioPlaybackStatus.ERROR
                                _errorMessage.value = "Speech synthesis error"
                            }
                        }
                    })
                }
            }
        } catch (_: Exception) {}
    }

    fun selectQari(qari: Qari) {
        _selectedQari.value = qari
        // If an Ayah track is currently playing, switch stream to new Qari seamlessly
        val track = _activeTrack.value
        if (track != null && track.type == AudioSourceType.QURAN_AYAH && _playbackStatus.value == AudioPlaybackStatus.PLAYING) {
            playQuranAyah(track.surahNumber, track.ayahNumber, track.totalAyahs, track.title, qari)
        }
    }

    fun playQuranAyah(
        surahNumber: Int,
        ayahNumber: Int,
        totalAyahs: Int,
        surahName: String,
        qari: Qari = _selectedQari.value
    ) {
        stopTtsIfSpeaking()
        val url = IslamicData.getAyahAudioUrl(qari, surahNumber, ayahNumber)
        val track = ActiveAudioTrack(
            id = "quran_${surahNumber}_$ayahNumber",
            title = "$surahName • Ayah $ayahNumber",
            subtitle = "${qari.name} (${qari.country})",
            type = AudioSourceType.QURAN_AYAH,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            totalAyahs = totalAyahs,
            audioUrl = url,
            isTts = false
        )
        _activeTrack.value = track
        onAyahChangedListener?.invoke(surahNumber, ayahNumber)
        streamUrl(url)
    }

    fun playSurahAudio(surah: Surah, qari: Qari = _selectedQari.value) {
        stopTtsIfSpeaking()
        val url = IslamicData.getSurahAudioUrl(qari, surah.number)
        val track = ActiveAudioTrack(
            id = "surah_${surah.number}",
            title = "Surah ${surah.nameEn} (${surah.nameArabic})",
            subtitle = "Recited by ${qari.name}",
            type = AudioSourceType.QURAN_SURAH,
            surahNumber = surah.number,
            ayahNumber = 1,
            totalAyahs = surah.totalAyahs,
            audioUrl = url,
            isTts = false
        )
        _activeTrack.value = track
        streamUrl(url)
    }

    fun playDuaRecitation(dua: Supplication) {
        stopTtsIfSpeaking()
        val url = dua.audioUrl ?: run {
            // If dua has no pre-defined URL, fallback to high-quality Arabic TTS pronunciation
            speakText(
                title = dua.titleEn,
                subtitle = "Arabic Pronunciation",
                text = dua.arabicText,
                languageCode = "ar"
            )
            return
        }

        val track = ActiveAudioTrack(
            id = "dua_${dua.id}",
            title = dua.titleEn,
            subtitle = "${dua.reference} • Authentic Recitation",
            type = AudioSourceType.DUA,
            audioUrl = url,
            isTts = false
        )
        _activeTrack.value = track
        streamUrl(url)
    }

    fun playHadithAudio(hadith: Hadith, language: String = "ar") {
        stopTtsIfSpeaking()
        val url = hadith.audioUrl
        if (url != null && language == "ar") {
            val track = ActiveAudioTrack(
                id = "hadith_${hadith.id}",
                title = "${hadith.book} • ${hadith.hadithNumber}",
                subtitle = "Arabic Narration",
                type = AudioSourceType.HADITH,
                audioUrl = url,
                isTts = false
            )
            _activeTrack.value = track
            streamUrl(url)
        } else {
            // TTS recitation for Arabic or multilingual translations
            val textToSpeak = when (language) {
                "ur" -> hadith.matnUr
                "hi" -> hadith.matnHi
                "en" -> hadith.matnEn
                else -> hadith.matnArabic
            }
            val subtitleLang = when (language) {
                "ur" -> "Urdu Translation Voice"
                "hi" -> "Hindi Translation Voice"
                "en" -> "English Translation Voice"
                else -> "Arabic Narration Voice"
            }
            speakText(
                title = "${hadith.book} • ${hadith.hadithNumber}",
                subtitle = subtitleLang,
                text = textToSpeak,
                languageCode = language
            )
        }
    }

    fun speakText(title: String, subtitle: String, text: String, languageCode: String) {
        stopMediaPlayer()
        if (!isTtsReady || textToSpeech == null) {
            _errorMessage.value = "Text-to-speech engine is initializing, please try again."
            return
        }

        val locale = when (languageCode) {
            "ar" -> Locale("ar")
            "ur" -> Locale("ur", "PK")
            "hi" -> Locale("hi", "IN")
            else -> Locale.ENGLISH
        }

        try {
            textToSpeech?.language = locale
            textToSpeech?.setSpeechRate(_playbackSpeed.value)

            val track = ActiveAudioTrack(
                id = "tts_${System.currentTimeMillis()}",
                title = title,
                subtitle = subtitle,
                type = AudioSourceType.DUA,
                textToSpeak = text,
                isTts = true
            )
            _activeTrack.value = track
            _playbackStatus.value = AudioPlaybackStatus.PLAYING
            _currentPositionMs.value = 0
            _durationMs.value = 10000 // Approximate duration for progress

            val params = android.os.Bundle()
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "NOOR_DEEN_TTS")
            startForegroundServiceIfAvailable()
        } catch (e: Exception) {
            _errorMessage.value = "Speech synthesis error: ${e.message}"
            _playbackStatus.value = AudioPlaybackStatus.ERROR
        }
    }

    private fun streamUrl(url: String) {
        try {
            _playbackStatus.value = AudioPlaybackStatus.BUFFERING
            _errorMessage.value = null
            stopProgressTracker()

            if (mediaPlayer == null) {
                initMediaPlayer()
            }

            mediaPlayer?.apply {
                reset()
                setDataSource(url)
                prepareAsync()
            }
        } catch (e: Exception) {
            _playbackStatus.value = AudioPlaybackStatus.ERROR
            _errorMessage.value = "Failed to stream audio: ${e.message}"
        }
    }

    fun togglePlayPause() {
        if (_playbackStatus.value == AudioPlaybackStatus.PLAYING) {
            pause()
        } else if (_playbackStatus.value == AudioPlaybackStatus.PAUSED) {
            resume()
        }
    }

    fun resume() {
        if (_activeTrack.value?.isTts == true) {
            // Re-speak if TTS
            val track = _activeTrack.value
            if (track != null) {
                speakText(track.title, track.subtitle, track.textToSpeak, "en")
            }
        } else {
            try {
                mediaPlayer?.let {
                    it.start()
                    _playbackStatus.value = AudioPlaybackStatus.PLAYING
                    startProgressTracker()
                    startForegroundServiceIfAvailable()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to resume: ${e.message}"
            }
        }
    }

    fun pause() {
        if (_activeTrack.value?.isTts == true) {
            textToSpeech?.stop()
            _ttsSpeaking.value = false
            _playbackStatus.value = AudioPlaybackStatus.PAUSED
        } else {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        it.pause()
                    }
                    _playbackStatus.value = AudioPlaybackStatus.PAUSED
                    stopProgressTracker()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to pause: ${e.message}"
            }
        }
    }

    fun stop() {
        stopMediaPlayer()
        stopTtsIfSpeaking()
        _activeTrack.value = null
        _playbackStatus.value = AudioPlaybackStatus.IDLE
        _currentPositionMs.value = 0
        _durationMs.value = 0
        stopForegroundServiceIfAvailable()
    }

    fun seekTo(positionMs: Int) {
        try {
            mediaPlayer?.seekTo(positionMs)
            _currentPositionMs.value = positionMs
        } catch (_: Exception) {}
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
        applyPlaybackSpeed()
        if (isTtsReady) {
            textToSpeech?.setSpeechRate(speed)
        }
    }

    private fun applyPlaybackSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying || _playbackStatus.value == AudioPlaybackStatus.PLAYING) {
                        mp.playbackParams = mp.playbackParams.setSpeed(_playbackSpeed.value)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    fun toggleLoop() {
        _isLooping.value = !_isLooping.value
    }

    fun toggleAutoAdvance() {
        _autoAdvanceAyah.value = !_autoAdvanceAyah.value
    }

    fun next() {
        val track = _activeTrack.value ?: return
        if (track.type == AudioSourceType.QURAN_AYAH) {
            if (track.ayahNumber < track.totalAyahs) {
                val nextAyah = track.ayahNumber + 1
                playQuranAyah(track.surahNumber, nextAyah, track.totalAyahs, track.title.substringBefore(" •"), _selectedQari.value)
            }
        }
    }

    fun previous() {
        val track = _activeTrack.value ?: return
        if (track.type == AudioSourceType.QURAN_AYAH) {
            if (track.ayahNumber > 1) {
                val prevAyah = track.ayahNumber - 1
                playQuranAyah(track.surahNumber, prevAyah, track.totalAyahs, track.title.substringBefore(" •"), _selectedQari.value)
            }
        }
    }

    private fun handlePlaybackCompleted() {
        stopProgressTracker()
        if (_isLooping.value) {
            // Replay same track
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            _playbackStatus.value = AudioPlaybackStatus.PLAYING
            startProgressTracker()
            return
        }

        val track = _activeTrack.value
        if (track?.type == AudioSourceType.QURAN_AYAH && _autoAdvanceAyah.value) {
            if (track.ayahNumber < track.totalAyahs) {
                // Auto-advance to next ayah seamlessly!
                val nextAyah = track.ayahNumber + 1
                playQuranAyah(track.surahNumber, nextAyah, track.totalAyahs, track.title.substringBefore(" •"), _selectedQari.value)
                return
            }
        }

        _playbackStatus.value = AudioPlaybackStatus.IDLE
        _currentPositionMs.value = 0
        stopForegroundServiceIfAvailable()
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive && _playbackStatus.value == AudioPlaybackStatus.PLAYING) {
                try {
                    mediaPlayer?.let { mp ->
                        if (mp.isPlaying) {
                            _currentPositionMs.value = mp.currentPosition
                            _durationMs.value = mp.duration.coerceAtLeast(0)
                        }
                    }
                } catch (_: Exception) {}
                delay(250L)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun stopMediaPlayer() {
        stopProgressTracker()
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
            }
        } catch (_: Exception) {}
    }

    private fun stopTtsIfSpeaking() {
        try {
            if (_ttsSpeaking.value) {
                textToSpeech?.stop()
                _ttsSpeaking.value = false
            }
        } catch (_: Exception) {}
    }

    private fun startForegroundServiceIfAvailable() {
        appContext?.let { ctx ->
            IslamicAudioPlaybackService.startService(ctx)
        }
    }

    private fun stopForegroundServiceIfAvailable() {
        appContext?.let { ctx ->
            IslamicAudioPlaybackService.stopService(ctx)
        }
    }

    fun release() {
        stop()
        mediaPlayer?.release()
        mediaPlayer = null
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
