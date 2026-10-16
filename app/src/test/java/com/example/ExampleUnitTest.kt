package com.example

import com.example.data.IslamicData
import com.example.model.AppLanguage
import com.example.model.CalculationMethod
import com.example.model.HadithTopic
import com.example.model.JuristicMethod
import com.example.model.TajweedType
import com.example.service.HadithSemanticEngine
import com.example.service.PrayerAndQiblaService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun testQiblaBearingCalculation() {
        val londonBearing = PrayerAndQiblaService.calculateQiblaBearing(51.5074, -0.1278)
        assertTrue("London Qibla bearing should be between 110° and 130°", londonBearing in 110.0..130.0)

        val cairoBearing = PrayerAndQiblaService.calculateQiblaBearing(30.0444, 31.2357)
        assertTrue("Cairo Qibla bearing should be between 130° and 145°", cairoBearing in 130.0..145.0)
    }

    @Test
    fun testPrayerTimesComputation() {
        val city = IslamicData.worldCities.first()
        val times = PrayerAndQiblaService.computePrayerTimes(
            city = city,
            calendar = Calendar.getInstance(),
            calculationMethod = CalculationMethod.KARACHI,
            juristicMethod = JuristicMethod.HANAFI
        )

        assertEquals("Should compute 6 times (5 prayers + sunrise)", 6, times.size)
        val names = times.map { it.nameEn }
        assertTrue(names.contains("Fajr"))
        assertTrue(names.contains("Dhuhr"))
        assertTrue(names.contains("Asr"))
        assertTrue(names.contains("Maghrib"))
        assertTrue(names.contains("Isha"))
    }

    @Test
    fun testSemanticSearchUnderstanding() {
        // Natural language query: "how should I treat my mother"
        val motherResults = HadithSemanticEngine.search(
            query = "how should I treat my mother",
            selectedTopic = null,
            selectedNarrator = null,
            selectedLanguage = AppLanguage.ENGLISH
        )
        assertTrue("Should return Hadith about paradise beneath mother's feet", motherResults.isNotEmpty())
        assertEquals("sn_3104", motherResults.first().id)

        // Natural language query: "importance of intentions in deeds"
        val intentionResults = HadithSemanticEngine.search(
            query = "importance of intentions in deeds",
            selectedTopic = null,
            selectedNarrator = null,
            selectedLanguage = AppLanguage.ENGLISH
        )
        assertTrue(intentionResults.isNotEmpty())
        assertEquals("sb_1", intentionResults.first().id)

        // Filter by Narrator
        val narratorResults = HadithSemanticEngine.search(
            query = "",
            selectedTopic = null,
            selectedNarrator = "Aisha (Mother of the Believers)",
            selectedLanguage = AppLanguage.ENGLISH
        )
        assertTrue(narratorResults.isNotEmpty())
        assertTrue(narratorResults.all { it.narrator.contains("Aisha") })
    }

    @Test
    fun testTajweedRulesIntegrity() {
        // Verify all 5 Tajweed rule definitions
        assertEquals(5, TajweedType.entries.size)
        assertTrue(TajweedType.entries.any { it == TajweedType.GHUNNAH })
        assertTrue(TajweedType.entries.any { it == TajweedType.IKHFA })
        assertTrue(TajweedType.entries.any { it == TajweedType.IDGHAM })
        assertTrue(TajweedType.entries.any { it == TajweedType.QALQALAH })
        assertTrue(TajweedType.entries.any { it == TajweedType.MADD })

        // Check Surah Al-Fatihah verses contain Tajweed spans
        val fatiha = IslamicData.fatihaVerses
        assertTrue(fatiha.any { it.tajweedSpans.isNotEmpty() })
    }

    @Test
    fun testAudioStreamingAndQarisIntegrity() {
        assertEquals(3, IslamicData.qaris.size)
        val qariIds = IslamicData.qaris.map { it.id }
        assertTrue(qariIds.contains("mishary"))
        assertTrue(qariIds.contains("sudais"))
        assertTrue(qariIds.contains("muaiqly"))

        val mishary = IslamicData.qaris.first { it.id == "mishary" }
        val fatihaAyah1Url = IslamicData.getAyahAudioUrl(mishary, 1, 1)
        assertEquals("https://everyayah.com/data/Alafasy_128kbps/001001.mp3", fatihaAyah1Url)

        val fourQuls = IslamicData.supplications.filter { it.category == com.example.model.DuaCategory.FOUR_QULS }
        assertEquals(4, fourQuls.size)

        val morningAdhkar = IslamicData.supplications.filter { it.category == com.example.model.DuaCategory.MORNING_ADHKAR }
        assertTrue(morningAdhkar.isNotEmpty())

        val eveningAdhkar = IslamicData.supplications.filter { it.category == com.example.model.DuaCategory.EVENING_ADHKAR }
        assertTrue(eveningAdhkar.isNotEmpty())
    }
}
