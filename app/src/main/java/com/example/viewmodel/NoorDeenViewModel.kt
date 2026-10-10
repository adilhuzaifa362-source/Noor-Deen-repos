package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.IslamicData
import com.example.model.AppLanguage
import com.example.model.AsmaName
import com.example.model.CalculationMethod
import com.example.model.ChatMessage
import com.example.model.ChatSender
import com.example.model.CityLocation
import com.example.service.AladhanPrayerApiService
import com.example.service.HadithSemanticEngine
import com.example.service.IslamicAiChatbotService
import com.example.service.LocationHelper
import java.util.UUID
import com.example.model.DuaCategory
import com.example.model.Hadith
import com.example.model.HadithTopic
import com.example.model.JuristicMethod
import com.example.model.MasjidJamaat
import com.example.model.PrayerTime
import com.example.model.Qari
import com.example.model.Supplication
import com.example.model.Surah
import com.example.model.TajweedType
import com.example.service.PrayerAndQiblaService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs

enum class NavigationTab {
    QURAN, DUAS, PRAYER, HADITH, TOOLS
}

enum class SubScreen {
    SURAH_READER,
    DUA_DETAIL,
    QIBLA_COMPASS,
    MASJID_NETWORK,
    ZAKAT_CALCULATOR,
    ASMA_UL_HUSNA,
    RAMADAN_KHATAM,
    DIGITAL_TASBEEH,
    HIJRI_CALENDAR,
    AI_SCHOLAR_CHAT
}

class NoorDeenViewModel(application: Application) : AndroidViewModel(application) {

    // Localization & Navigation
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentTab = MutableStateFlow(NavigationTab.QURAN)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _currentSubScreen = MutableStateFlow<SubScreen?>(null)
    val currentSubScreen: StateFlow<SubScreen?> = _currentSubScreen.asStateFlow()

    // Quran & Recitation Sciences
    private val _selectedSurah = MutableStateFlow(IslamicData.allSurahs.first())
    val selectedSurah: StateFlow<Surah> = _selectedSurah.asStateFlow()

    private val _isSideBySideView = MutableStateFlow(false)
    val isSideBySideView: StateFlow<Boolean> = _isSideBySideView.asStateFlow()

    private val _isTajweedEnabled = MutableStateFlow(true)
    val isTajweedEnabled: StateFlow<Boolean> = _isTajweedEnabled.asStateFlow()

    private val _selectedTajweedLegend = MutableStateFlow<TajweedType?>(null)
    val selectedTajweedLegend: StateFlow<TajweedType?> = _selectedTajweedLegend.asStateFlow()

    private val _selectedQari = MutableStateFlow(IslamicData.qaris.first())
    val selectedQari: StateFlow<Qari> = _selectedQari.asStateFlow()

    private val _isPlayingAudio = MutableStateFlow(false)
    val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

    private val _currentPlayingAyah = MutableStateFlow(1)
    val currentPlayingAyah: StateFlow<Int> = _currentPlayingAyah.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _audioProgress = MutableStateFlow(0f)
    val audioProgress: StateFlow<Float> = _audioProgress.asStateFlow()

    private var audioJob: Job? = null

    // Duas & Adhkar
    private val _selectedDuaCategory = MutableStateFlow<DuaCategory?>(null)
    val selectedDuaCategory: StateFlow<DuaCategory?> = _selectedDuaCategory.asStateFlow()

    private val _duaSearchQuery = MutableStateFlow("")
    val duaSearchQuery: StateFlow<String> = _duaSearchQuery.asStateFlow()

    private val _selectedDua = MutableStateFlow<Supplication?>(null)
    val selectedDua: StateFlow<Supplication?> = _selectedDua.asStateFlow()

    private val _supplicationCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val supplicationCounts: StateFlow<Map<String, Int>> = _supplicationCounts.asStateFlow()

    private val _bookmarkedDuaIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedDuaIds: StateFlow<Set<String>> = _bookmarkedDuaIds.asStateFlow()

    // Prayer & Qibla
    private val _selectedCity = MutableStateFlow(IslamicData.worldCities.first())
    val selectedCity: StateFlow<CityLocation> = _selectedCity.asStateFlow()

    private val _calculationMethod = MutableStateFlow(CalculationMethod.MWL)
    val calculationMethod: StateFlow<CalculationMethod> = _calculationMethod.asStateFlow()

    private val _juristicMethod = MutableStateFlow(JuristicMethod.SHAFI_STANDARD)
    val juristicMethod: StateFlow<JuristicMethod> = _juristicMethod.asStateFlow()

    private val _prayerTimes = MutableStateFlow<List<PrayerTime>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTime>> = _prayerTimes.asStateFlow()

    private val _qiblaBearing = MutableStateFlow(0.0)
    val qiblaBearing: StateFlow<Double> = _qiblaBearing.asStateFlow()

    private val _deviceHeading = MutableStateFlow(0f)
    val deviceHeading: StateFlow<Float> = _deviceHeading.asStateFlow()

    private val _sensorAccuracy = MutableStateFlow(3)
    val sensorAccuracy: StateFlow<Int> = _sensorAccuracy.asStateFlow()

    private val _isFacingQibla = MutableStateFlow(false)
    val isFacingQibla: StateFlow<Boolean> = _isFacingQibla.asStateFlow()

    private val _masjids = MutableStateFlow(IslamicData.sampleMasjids)
    val masjids: StateFlow<List<MasjidJamaat>> = _masjids.asStateFlow()

    private var compassManager: PrayerAndQiblaService.CompassSensorManager? = null

    // Live Location & Aladhan Public API Sync
    private val _isLiveLocationActive = MutableStateFlow(false)
    val isLiveLocationActive: StateFlow<Boolean> = _isLiveLocationActive.asStateFlow()

    private val _liveCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val liveCoordinates: StateFlow<Pair<Double, Double>?> = _liveCoordinates.asStateFlow()

    private val _apiSyncLoading = MutableStateFlow(false)
    val apiSyncLoading: StateFlow<Boolean> = _apiSyncLoading.asStateFlow()

    private val _apiSyncStatusMessage = MutableStateFlow("Local Astronomical Engine")
    val apiSyncStatusMessage: StateFlow<String> = _apiSyncStatusMessage.asStateFlow()

    private val _apiHijriDate = MutableStateFlow<String?>(null)
    val apiHijriDate: StateFlow<String?> = _apiHijriDate.asStateFlow()

    private val _apiTimezone = MutableStateFlow<String?>(null)
    val apiTimezone: StateFlow<String?> = _apiTimezone.asStateFlow()

    // Hadith
    private val _selectedHadithTopic = MutableStateFlow<HadithTopic?>(null)
    val selectedHadithTopic: StateFlow<HadithTopic?> = _selectedHadithTopic.asStateFlow()

    private val _selectedNarrator = MutableStateFlow("All Narrators")
    val selectedNarrator: StateFlow<String> = _selectedNarrator.asStateFlow()

    private val _hadithSearchQuery = MutableStateFlow("")
    val hadithSearchQuery: StateFlow<String> = _hadithSearchQuery.asStateFlow()

    private val _selectedHadith = MutableStateFlow<Hadith?>(null)
    val selectedHadith: StateFlow<Hadith?> = _selectedHadith.asStateFlow()

    private val _filteredHadiths = MutableStateFlow(HadithSemanticEngine.hadiths)
    val filteredHadiths: StateFlow<List<Hadith>> = _filteredHadiths.asStateFlow()

    // AI Scholar Chatbot
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome_msg",
                sender = ChatSender.AI_SCHOLAR,
                text = "Assalamu Alaikum wa Rahmatullah. I am Noor Deen AI Scholar (عالم نور الدين). Ask me any question, and I will answer with verified evidence directly from the Holy Quran and authentic Sahih Hadith (Sahih Sitta).",
                references = listOf("Surah Ali 'Imran 3:31", "Sahih al-Bukhari 1")
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Tools & Life Utilities
    // Asma-ul-Husna
    private val _isAsmaFlashcardMode = MutableStateFlow(false)
    val isAsmaFlashcardMode: StateFlow<Boolean> = _isAsmaFlashcardMode.asStateFlow()

    private val _currentFlashcardIndex = MutableStateFlow(0)
    val currentFlashcardIndex: StateFlow<Int> = _currentFlashcardIndex.asStateFlow()

    private val _showFlashcardBack = MutableStateFlow(false)
    val showFlashcardBack: StateFlow<Boolean> = _showFlashcardBack.asStateFlow()

    private val _memorizedAsmaNumbers = MutableStateFlow<Set<Int>>(setOf(1, 2, 3, 4, 5))
    val memorizedAsmaNumbers: StateFlow<Set<Int>> = _memorizedAsmaNumbers.asStateFlow()

    // Zakat
    val cashOnHand = MutableStateFlow("1500")
    val bankSavings = MutableStateFlow("4500")
    val goldGrams = MutableStateFlow("25")
    val goldPricePerGram = MutableStateFlow("85.0")
    val silverGrams = MutableStateFlow("100")
    val silverPricePerGram = MutableStateFlow("1.10")
    val businessMerchandise = MutableStateFlow("0")
    val shortTermLiabilities = MutableStateFlow("500")
    val zakatCurrency = MutableStateFlow("USD")

    // Ramadan & Khatam Planner
    val khatamTargetDays = MutableStateFlow(30)
    val pagesRead = MutableStateFlow(124)
    val totalQuranPages = 604
    private val _completedFasts = MutableStateFlow<Set<Int>>((1..12).toSet())
    val completedFasts: StateFlow<Set<Int>> = _completedFasts.asStateFlow()

    // Digital Tasbeeh
    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val _tasbeehTarget = MutableStateFlow(33)
    val tasbeehTarget: StateFlow<Int> = _tasbeehTarget.asStateFlow()

    private val _selectedDhikrPreset = MutableStateFlow("SubhanAllah")
    val selectedDhikrPreset: StateFlow<String> = _selectedDhikrPreset.asStateFlow()

    private val _tasbeehTotalCycles = MutableStateFlow(14)
    val tasbeehTotalCycles: StateFlow<Int> = _tasbeehTotalCycles.asStateFlow()

    init {
        updatePrayerTimes()
        initCompass()
        refreshPrayerTimes()
    }

    // Localization
    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    // Navigation
    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
        _currentSubScreen.value = null
    }

    fun openSubScreen(subScreen: SubScreen) {
        _currentSubScreen.value = subScreen
    }

    fun navigateBack() {
        if (_currentSubScreen.value != null) {
            _currentSubScreen.value = null
        }
    }

    // Quran Actions
    fun openSurah(surah: Surah) {
        _selectedSurah.value = surah
        _currentPlayingAyah.value = 1
        _isPlayingAudio.value = false
        audioJob?.cancel()
        _currentSubScreen.value = SubScreen.SURAH_READER
    }

    fun toggleSideBySideView() {
        _isSideBySideView.value = !_isSideBySideView.value
    }

    fun toggleTajweed() {
        _isTajweedEnabled.value = !_isTajweedEnabled.value
    }

    fun showTajweedLegend(rule: TajweedType?) {
        _selectedTajweedLegend.value = rule
    }

    fun selectQari(qari: Qari) {
        _selectedQari.value = qari
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun toggleAudioPlayback() {
        if (_isPlayingAudio.value) {
            pauseAudio()
        } else {
            playAudio()
        }
    }

    private fun playAudio() {
        _isPlayingAudio.value = true
        audioJob?.cancel()
        audioJob = viewModelScope.launch {
            val totalAyahs = _selectedSurah.value.verses.size.coerceAtLeast(1)
            while (isActive && _isPlayingAudio.value) {
                // simulate verse audio playback with progress
                val stepDuration = ((3500L / _playbackSpeed.value) / 20).toLong()
                for (step in 1..20) {
                    delay(stepDuration)
                    _audioProgress.value = step / 20f
                }
                _audioProgress.value = 0f
                if (_currentPlayingAyah.value < totalAyahs) {
                    _currentPlayingAyah.value += 1
                } else {
                    _currentPlayingAyah.value = 1
                    _isPlayingAudio.value = false
                    break
                }
            }
        }
    }

    fun pauseAudio() {
        _isPlayingAudio.value = false
        audioJob?.cancel()
    }

    fun nextAyah() {
        val total = _selectedSurah.value.verses.size.coerceAtLeast(1)
        if (_currentPlayingAyah.value < total) {
            _currentPlayingAyah.value += 1
            _audioProgress.value = 0f
        }
    }

    fun previousAyah() {
        if (_currentPlayingAyah.value > 1) {
            _currentPlayingAyah.value -= 1
            _audioProgress.value = 0f
        }
    }

    // Duas & Wazifas Actions
    fun filterDuaCategory(category: DuaCategory?) {
        _selectedDuaCategory.value = category
    }

    fun setDuaSearchQuery(query: String) {
        _duaSearchQuery.value = query
    }

    fun openDuaDetail(supplication: Supplication) {
        _selectedDua.value = supplication
        _currentSubScreen.value = SubScreen.DUA_DETAIL
    }

    fun incrementDuaCount(duaId: String, targetCount: Int) {
        val current = _supplicationCounts.value[duaId] ?: 0
        val next = current + 1
        _supplicationCounts.value = _supplicationCounts.value + (duaId to next)
        vibrate(50)
        if (next == targetCount) {
            vibrate(150)
        }
    }

    fun resetDuaCount(duaId: String) {
        _supplicationCounts.value = _supplicationCounts.value + (duaId to 0)
    }

    fun toggleBookmarkDua(duaId: String) {
        val current = _bookmarkedDuaIds.value
        _bookmarkedDuaIds.value = if (current.contains(duaId)) {
            current - duaId
        } else {
            current + duaId
        }
    }

    // Prayer & Qibla Actions
    fun setCity(city: CityLocation) {
        _selectedCity.value = city
        _isLiveLocationActive.value = false
        _liveCoordinates.value = null
        updatePrayerTimes()
        fetchPrayerTimesFromApi(city.latitude, city.longitude, "${city.name}, ${city.country}")
    }

    fun setCalculationMethod(method: CalculationMethod) {
        _calculationMethod.value = method
        updatePrayerTimes()
        refreshPrayerTimes()
    }

    fun setJuristicMethod(method: JuristicMethod) {
        _juristicMethod.value = method
        updatePrayerTimes()
        refreshPrayerTimes()
    }

    fun fetchPrayerTimesFromApi(latitude: Double, longitude: Double, locationLabel: String? = null) {
        _apiSyncLoading.value = true
        viewModelScope.launch {
            val result = AladhanPrayerApiService.fetchTimingsByCoordinates(
                latitude = latitude,
                longitude = longitude,
                calculationMethod = _calculationMethod.value,
                juristicMethod = _juristicMethod.value
            )
            result.onSuccess { data ->
                _prayerTimes.value = data.prayerTimes
                _apiHijriDate.value = data.hijriDateFormatted
                _apiTimezone.value = data.timezone
                _apiSyncStatusMessage.value = "Synced with Aladhan API (${data.methodName})"
                _qiblaBearing.value = PrayerAndQiblaService.calculateQiblaBearing(latitude, longitude)
                if (locationLabel != null) {
                    _liveCoordinates.value = Pair(latitude, longitude)
                }
                _apiSyncLoading.value = false
            }.onFailure { error ->
                updatePrayerTimes()
                _apiSyncStatusMessage.value = "Local Astronomical Engine (Offline/Fallback)"
                _apiSyncLoading.value = false
            }
        }
    }

    fun fetchPrayerTimesForCurrentLocation(context: Context) {
        _apiSyncLoading.value = true
        LocationHelper.getUserCurrentLocation(
            context = context,
            onLocationReceived = { lat, lon ->
                _liveCoordinates.value = Pair(lat, lon)
                _isLiveLocationActive.value = true
                fetchPrayerTimesFromApi(
                    latitude = lat,
                    longitude = lon,
                    locationLabel = "Live GPS (${String.format(java.util.Locale.US, "%.2f", lat)}, ${String.format(java.util.Locale.US, "%.2f", lon)})"
                )
            },
            onError = { errMsg ->
                _apiSyncStatusMessage.value = "GPS Location: $errMsg. Using ${_selectedCity.value.name}."
                _apiSyncLoading.value = false
                updatePrayerTimes()
            }
        )
    }

    fun refreshPrayerTimes() {
        val coords = _liveCoordinates.value
        if (_isLiveLocationActive.value && coords != null) {
            fetchPrayerTimesFromApi(coords.first, coords.second, "Live GPS Location")
        } else {
            val city = _selectedCity.value
            fetchPrayerTimesFromApi(city.latitude, city.longitude, "${city.name}, ${city.country}")
        }
    }

    private fun updatePrayerTimes() {
        val times = PrayerAndQiblaService.computePrayerTimes(
            city = _selectedCity.value,
            calendar = Calendar.getInstance(),
            calculationMethod = _calculationMethod.value,
            juristicMethod = _juristicMethod.value
        )
        _prayerTimes.value = times
        val bearing = PrayerAndQiblaService.calculateQiblaBearing(
            _selectedCity.value.latitude,
            _selectedCity.value.longitude
        )
        _qiblaBearing.value = bearing
    }

    private fun initCompass() {
        val app = getApplication<Application>()
        compassManager = PrayerAndQiblaService.CompassSensorManager(app) { heading, accuracy ->
            _deviceHeading.value = heading
            _sensorAccuracy.value = accuracy
            val diff = abs(heading - _qiblaBearing.value.toFloat())
            val normalizedDiff = if (diff > 180) 360 - diff else diff
            val facing = normalizedDiff <= 3.5f
            if (facing && !_isFacingQibla.value) {
                vibrate(70)
            }
            _isFacingQibla.value = facing
        }
    }

    fun startCompass() {
        compassManager?.startListening()
    }

    fun stopCompass() {
        compassManager?.stopListening()
    }

    // Hadith Actions
    fun setHadithTopic(topic: HadithTopic?) {
        _selectedHadithTopic.value = topic
        updateHadithResults()
    }

    fun setHadithNarrator(narrator: String) {
        _selectedNarrator.value = narrator
        updateHadithResults()
    }

    fun setHadithSearchQuery(query: String) {
        _hadithSearchQuery.value = query
        updateHadithResults()
    }

    fun updateHadithResults() {
        _filteredHadiths.value = HadithSemanticEngine.search(
            query = _hadithSearchQuery.value,
            selectedTopic = _selectedHadithTopic.value,
            selectedNarrator = _selectedNarrator.value,
            selectedLanguage = _currentLanguage.value
        )
    }

    fun selectHadith(hadith: Hadith?) {
        _selectedHadith.value = hadith
    }

    // AI Scholar Chatbot Actions
    fun sendChatMessage(query: String) {
        if (query.isBlank() || _isChatLoading.value) return
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = ChatSender.USER,
            text = query.trim()
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val response = IslamicAiChatbotService.askAiScholar(
                userQuery = query.trim(),
                conversationHistory = _chatMessages.value
            )
            _chatMessages.value = _chatMessages.value + response
            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = ChatSender.AI_SCHOLAR,
                text = "Chat cleared. Feel free to ask any question regarding the Holy Quran and authentic Sunnah.",
                references = listOf("Surah Al-Baqarah 2:255")
            )
        )
    }

    // Asma-ul-Husna Flashcards
    fun toggleAsmaFlashcardMode() {
        _isAsmaFlashcardMode.value = !_isAsmaFlashcardMode.value
        _showFlashcardBack.value = false
    }

    fun nextFlashcard() {
        val total = IslamicData.asmaUlHusna.size
        _currentFlashcardIndex.value = (_currentFlashcardIndex.value + 1) % total
        _showFlashcardBack.value = false
    }

    fun prevFlashcard() {
        val total = IslamicData.asmaUlHusna.size
        _currentFlashcardIndex.value = (_currentFlashcardIndex.value - 1 + total) % total
        _showFlashcardBack.value = false
    }

    fun flipFlashcard() {
        _showFlashcardBack.value = !_showFlashcardBack.value
    }

    fun toggleMemorizedAsma(number: Int) {
        val set = _memorizedAsmaNumbers.value
        _memorizedAsmaNumbers.value = if (set.contains(number)) set - number else set + number
    }

    // Fasting Log
    fun toggleFastCompleted(day: Int) {
        val set = _completedFasts.value
        _completedFasts.value = if (set.contains(day)) set - day else set + day
    }

    fun updatePagesRead(pages: Int) {
        pagesRead.value = pages.coerceIn(0, totalQuranPages)
    }

    // Digital Tasbeeh Actions
    fun incrementTasbeeh() {
        val next = _tasbeehCount.value + 1
        val target = _tasbeehTarget.value
        vibrate(35)
        if (target > 0 && next >= target) {
            _tasbeehCount.value = 0
            _tasbeehTotalCycles.value += 1
            vibrate(180)
        } else {
            _tasbeehCount.value = next
        }
    }

    fun resetTasbeeh() {
        _tasbeehCount.value = 0
    }

    fun setTasbeehTarget(target: Int) {
        _tasbeehTarget.value = target
        _tasbeehCount.value = 0
    }

    fun setTasbeehPreset(preset: String) {
        _selectedDhikrPreset.value = preset
        _tasbeehCount.value = 0
    }

    // Volunteer Masjid Update
    fun updateMasjidJamaatTime(masjidId: String, prayerName: String, newJamaatTime: String) {
        _masjids.value = _masjids.value.map { m ->
            if (m.id == masjidId) {
                when (prayerName.lowercase()) {
                    "fajr" -> m.copy(fajrJamaat = newJamaatTime, lastVerifiedBy = "Community Verified (Just Now)")
                    "dhuhr" -> m.copy(dhuhrJamaat = newJamaatTime, lastVerifiedBy = "Community Verified (Just Now)")
                    "asr" -> m.copy(asrJamaat = newJamaatTime, lastVerifiedBy = "Community Verified (Just Now)")
                    "maghrib" -> m.copy(maghribJamaat = newJamaatTime, lastVerifiedBy = "Community Verified (Just Now)")
                    "isha" -> m.copy(ishaJamaat = newJamaatTime, lastVerifiedBy = "Community Verified (Just Now)")
                    else -> m
                }
            } else m
        }
    }

    private fun vibrate(durationMs: Long) {
        val app = getApplication<Application>()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
            // Graceful fallback if vibration unavailable
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioJob?.cancel()
        compassManager?.stopListening()
    }
}
