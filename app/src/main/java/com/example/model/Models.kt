package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.TajweedGhunnahColor
import com.example.ui.theme.TajweedIdghamColor
import com.example.ui.theme.TajweedIkhfaColor
import com.example.ui.theme.TajweedMaddColor
import com.example.ui.theme.TajweedQalqalahColor

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    ARABIC("ar", "العربية", true),
    ENGLISH("en", "English", false),
    URDU("ur", "اردو", true),
    HINDI("hi", "हिन्दी", false)
}

enum class RevelationType {
    MAKKI, MADANI
}

enum class TajweedType(
    val title: String,
    val arabicTitle: String,
    val description: String,
    val color: Color
) {
    GHUNNAH("Ghunnah", "غُنَّة", "Nasal sound held for 2 counts on Noon or Meem with Shaddah (نّ / مّ)", TajweedGhunnahColor),
    IKHFA("Ikhfa", "إِخْفَاء", "Concealment of Noon Sakinah or Tanween before 15 specific letters", TajweedIkhfaColor),
    IDGHAM("Idgham", "إِدْغَام", "Merging Noon Sakinah/Tanween into Yarmaloon letters (ي، ر، م، ل، و، ن)", TajweedIdghamColor),
    QALQALAH("Qalqalah", "قَلْقَلَة", "Echoing/bouncing vibration when stopping on Qutb Jad (ق، ط، ب، ج، د)", TajweedQalqalahColor),
    MADD("Madd", "مَدّ", "Prolongation of sound for 4 to 6 Harakat (counts)", TajweedMaddColor)
}

data class TajweedSpan(
    val start: Int,
    val end: Int,
    val type: TajweedType
)

data class Verse(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val transliteration: String,
    val translationEn: String,
    val translationUr: String,
    val translationHi: String,
    val tajweedSpans: List<TajweedSpan> = emptyList(),
    val audioDurationMs: Long = 4000L
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEn: String,
    val nameUr: String,
    val nameHi: String,
    val meaningEn: String,
    val totalAyahs: Int,
    val revelationType: RevelationType,
    val verses: List<Verse> = emptyList()
)

data class Qari(
    val id: String,
    val name: String,
    val country: String,
    val style: String
)

enum class HadithTopic(val displayNameEn: String, val displayNameUr: String, val displayNameHi: String, val displayNameAr: String) {
    FAITH("Faith & Tawheed", "ایمان و توحید", "ईमान और तौहीद", "الإيمان والتوحيد"),
    PRAYER("Prayer & Taharah", "نماز و طہارت", "नमाज़ और तहारत", "الصلاة والطهارة"),
    AKHLAQ("Good Character", "حسن اخلاق", "उत्तम चरित्र", "الأخلاق وحسن الخلق"),
    FASTING("Fasting & Ramadan", "روزہ و رمضان", "रोज़ा और रमज़ान", "الصوم ورمضان"),
    SADAQAH("Charity & Zakat", "صدقہ و زکوٰۃ", "दान और ज़कात", "الزكاة والصدقة"),
    FAMILY("Kinship & Family", "صلہ رحمی و حقوق", "परिवार व रिश्तेदारी", "صلة الرحم والأسرة")
}

data class Hadith(
    val id: String,
    val book: String,
    val hadithNumber: String,
    val chapter: String,
    val topic: HadithTopic,
    val grading: String, // "Sahih", "Hasan"
    val narrator: String = "Abu Huraira",
    val sanad: String,
    val matnArabic: String,
    val matnEn: String,
    val matnUr: String,
    val matnHi: String,
    val semanticKeywords: List<String> = emptyList()
)

enum class ChatSender {
    USER, AI_SCHOLAR
}

data class ChatMessage(
    val id: String,
    val sender: ChatSender,
    val text: String,
    val references: List<String> = emptyList(),
    val timestampMs: Long = System.currentTimeMillis()
)

enum class DuaCategory(val titleEn: String, val titleUr: String, val titleHi: String, val titleAr: String) {
    AYAT_UL_KURSI("Ayat al-Kursi", "آیۃ الکرسی", "आयतुल कुर्सी", "آية الكرسي"),
    AMANAR_RASUL("Amanar-Rasul", "آمن الرسول", "आमनर-रसूल", "آمن الرسول"),
    FOUR_QULS("4 Quls", "چار قل", "चार कुल", "الأربع قلاقل"),
    ESSENTIAL_DUAS("Essential Duas", "مسنون دعائیں", "ज़रूरी दुआएं", "أدعية مأثورة"),
    WAZIFAS("Authentic Wazifas", "وظائف مسنونہ", "मसनून वज़ीफ़े", "الأذكار والوظائف"),
    MORNING_ADHKAR("Morning Adhkar", "صبح کے اذکار", "सुबह के अज़कार", "أذكار الصباح"),
    EVENING_ADHKAR("Evening Adhkar", "شام کے اذکار", "शाम के अज़कार", "أذكار المساء")
}

data class Supplication(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleHi: String,
    val titleAr: String,
    val category: DuaCategory,
    val arabicText: String,
    val transliteration: String,
    val translationEn: String,
    val translationUr: String,
    val translationHi: String,
    val virtue: String,
    val reference: String,
    val targetCount: Int = 1,
    val currentCount: Int = 0,
    val tajweedSpans: List<TajweedSpan> = emptyList()
)

data class PrayerTime(
    val nameEn: String,
    val nameUr: String,
    val nameHi: String,
    val nameAr: String,
    val timeFormatted: String,
    val isNext: Boolean = false,
    val isPast: Boolean = false
)

enum class CalculationMethod(val displayName: String, val fajrAngle: Double, val ishaAngle: Double) {
    MWL("Muslim World League (MWL)", 18.0, 17.0),
    ISNA("Islamic Society of North America (ISNA)", 15.0, 15.0),
    UMM_AL_QURA("Umm al-Qura University (Makkah)", 18.5, 90.0), // 90 min after Maghrib
    KARACHI("Univ. of Islamic Sciences, Karachi", 18.0, 18.0),
    EGYPT("Egyptian General Authority", 19.5, 17.5)
}

enum class JuristicMethod(val displayName: String) {
    SHAFI_STANDARD("Shafi'i, Maliki, Hanbali (Standard Shadow 1x)"),
    HANAFI("Hanafi (Later Asr Shadow 2x)")
}

data class CityLocation(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneOffsetHours: Double
)

data class MasjidJamaat(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val address: String,
    val fajrAzan: String,
    val fajrJamaat: String,
    val dhuhrAzan: String,
    val dhuhrJamaat: String,
    val asrAzan: String,
    val asrJamaat: String,
    val maghribAzan: String,
    val maghribJamaat: String,
    val ishaAzan: String,
    val ishaJamaat: String,
    val lastVerifiedBy: String
)

data class AsmaName(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningUr: String,
    val meaningHi: String,
    val reflection: String,
    val quranReference: String,
    val isMemorized: Boolean = false
)

data class HijriDateEvent(
    val hijriDayMonth: String,
    val gregorianEstimate: String,
    val nameEn: String,
    val nameUr: String,
    val nameHi: String,
    val nameAr: String,
    val significance: String,
    val isFastingRecommended: Boolean
)
