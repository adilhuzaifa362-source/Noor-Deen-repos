# Implementation Plan: Quran, Hadith & Dua Audio Player with Background Streaming

## Overview
Implement a comprehensive, production-grade audio player system for the NoorDeen Android Islamic application:
1. **Quran Recitation Streaming**: Verse-by-verse and surah-level streaming recitation by the 3 selected world-renowned Qaris:
   - **Sheikh Mishary Rashid Alafasy** (Kuwait - Murattal)
   - **Sheikh Abdur-Rahman As-Sudais** (Chief Imam of Masjid al-Haram, Makkah)
   - **Sheikh Maher Al-Muaiqly** (Imam of Masjid al-Haram, Makkah)
2. **Background Playback Support**: Foreground service with MediaSession, audio focus management, wake lock, and system notification controls to keep recitation playing smoothly when navigating across tabs, locking the screen, or placing the app in the background.
3. **In-Page Audio Controls**:
   - **Quran Reader**: In-page sticky audio player with progress bar, buffering indicators, ayah-by-ayah synchronized highlighting, Qari switch modal, playback speed, and per-ayah instant play buttons.
   - **Duas & Azkar**: In-page audio player on Dua cards and detail screen with streamed authentic Arabic audio and TTS translation voice in English, Urdu, and Hindi.
   - **Hadith Reader**: In-page audio player on Hadith cards and detail modal with Arabic narration playback and multilingual translation speech.
4. **Duas & Azkar Resolution**: Populate all missing Duas categories (`FOUR_QULS`, `MORNING_ADHKAR`, `EVENING_ADHKAR`), resolve null-selection blank screen issues, and ensure smooth counter/tasbih integration.

---

## Architecture & Implementation Steps

### Step 1: Background Service & Audio Engine (`com.example.service`)
- **`IslamicAudioPlaybackService.kt`**:
  - Android Foreground Service managing `MediaPlayer` for reliable streaming.
  - Media notification with Play/Pause, Previous/Next ayah, and Stop actions.
  - Wake lock and Wi-Fi lock handling for uninterrupted background streaming.
  - Audio focus request/abandonment on calls or other media.
- **`AudioPlaybackManager.kt`**:
  - Centralized singleton managing audio playback state (Surah, Ayah, Dua, Hadith).
  - Exposes reactive `StateFlow`s: `playbackState`, `currentTrackInfo`, `currentPositionMs`, `durationMs`, `bufferedPercentage`, `selectedQari`, `playbackSpeed`.
  - Methods: `playQuranAyah(surahNumber, ayahNumber, qari)`, `playDua(duaId, audioUrl)`, `speakText(text, locale)`, `pause()`, `resume()`, `seekTo(positionMs)`, `next()`, `previous()`.
  - Seamless fallback to Android `TextToSpeech` for translations and text-based pronunciations.

### Step 2: Manifest & Permissions Updates (`AndroidManifest.xml`)
- Add required permissions:
  - `android.permission.FOREGROUND_SERVICE`
  - `android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK`
  - `android.permission.WAKE_LOCK`
  - `android.permission.POST_NOTIFICATIONS`
- Register `IslamicAudioPlaybackService` with `foregroundServiceType="mediaPlayback"`.

### Step 3: Complete Missing Islamic Data & Stream URLs (`IslamicData.kt`)
- Add Qari streaming URL resolvers:
  - EveryAyah 128kbps/192kbps verse-level CDNs for Mishary Alafasy, Abdur-Rahman As-Sudais, and Maher Al-Muaiqly.
  - MP3Quran server high-fidelity audio streams for continuous surah playback.
- Populate missing Duas & Azkar:
  - Complete `FOUR_QULS` (Al-Kafirun, Al-Ikhlas, Al-Falaq, An-Nas)
  - Complete `MORNING_ADHKAR` (Asbahna wa Asbahal Mulk, Allahumma Bika Asbahna, Sayyidul Istighfar, Ayat al-Kursi, Bismillahilladhi..., Subhanallahi wa bihamdihi...)
  - Complete `EVENING_ADHKAR` (Amsayna wa Amsal Mulk, Allahumma Bika Amsayna, A'udhu bi kalimatillahi..., etc.)
  - Authentic audio stream URLs and phonetic Arabic for each supplication.

### Step 4: ViewModel Integration (`NoorDeenViewModel.kt`)
- Connect `NoorDeenViewModel` to `AudioPlaybackManager`.
- Replace simulated mock loop with real streaming playback and auto-advancing ayah synchronization.
- Expose state flows for player status, current ayah, buffering, Qari selection, speed, and TTS translation audio.

### Step 5: In-Page Audio Player UI Implementation
- **`AudioPlayerBar.kt` & `QuranScreen.kt`**:
  - In-page reader player with active seekbar, elapsed/total time, Qari selector sheet/dialog with preview avatars, speed selector, and loop toggle.
  - Inline play icon button on each verse row in `SurahReaderView` to play any ayah immediately with live visual glow.
- **`DuasScreen.kt`**:
  - In-page audio player bar on Dua cards and inside `DuaDetailReaderView`.
  - Dual options: "Play Arabic Recitation" and "Listen Translation (TTS)".
- **`HadithScreen.kt`**:
  - In-page audio player on Hadith cards and detail dialog:
  - Arabic narration stream / speech + translation voice in user's active language.

### Step 6: Verification & Compilation
- Run `compile_applet` to verify compilation.
- Ensure all screen interactions, playback events, and background lifecycle functions operate seamlessly.
