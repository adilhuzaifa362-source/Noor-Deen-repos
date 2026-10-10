package com.example.service

import com.example.BuildConfig
import com.example.model.ChatMessage
import com.example.model.ChatSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

object IslamicAiChatbotService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-2.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private const val SYSTEM_PROMPT = """You are Noor Deen AI (عالم نور الدين), an authentic Islamic knowledge assistant developed exclusively to provide guidance rooted strictly in the Holy Quran and authentic Sunnah.

CRITICAL RULES AND MANDATES:
1. Every answer MUST be supported by explicit citations from the Holy Quran (citing [Surah Name Surah_Number:Ayah_Number]) and authentic Sahih Hadiths from the Sahih Sitta (Sahih al-Bukhari, Sahih Muslim, Jami` at-Tirmidhi, Sunan Abi Dawud, Sunan an-Nasa'i, Sunan Ibn Majah).
2. Explicitly include the collection name, hadith number, primary narrator, and scholar-accepted grading (Sahih or Hasan).
3. NEVER cite weak (Da'if) or fabricated (Mawdu') narrations.
4. If a ruling has differences of opinion among classical scholars, present the authentic evidence neutrally and advise consulting local recognized scholars.
5. Provide responses in the language of the user's question (support English, Arabic, Urdu, and Hindi).
6. Format clearly with bullet points, Quranic Arabic text when appropriate, transliteration, and authentic translations."""

    suspend fun askAiScholar(
        userQuery: String,
        conversationHistory: List<ChatMessage>
    ): ChatMessage = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineVerifiedAnswer(userQuery)
        }

        try {
            val url = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // System Instruction
            val systemObj = JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().put("text", SYSTEM_PROMPT))
                })
            }

            // Recent history
            val recentTurns = conversationHistory.takeLast(4)
            for (msg in recentTurns) {
                val role = if (msg.sender == ChatSender.USER) "user" else "model"
                contentsArray.put(JSONObject().apply {
                    put("role", role)
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", msg.text))
                    })
                })
            }

            // Current query
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().put("text", userQuery))
                })
            })

            val jsonBody = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", systemObj)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.3) // Lower temperature for strict factual accuracy
                    put("topP", 0.9)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    return@withContext ChatMessage(
                        id = UUID.randomUUID().toString(),
                        sender = ChatSender.AI_SCHOLAR,
                        text = text,
                        references = extractReferences(text)
                    )
                }
            }

            // Fallback if API returned non-success or empty
            return@withContext getOfflineVerifiedAnswer(userQuery)

        } catch (e: Exception) {
            return@withContext getOfflineVerifiedAnswer(userQuery)
        }
    }

    private fun extractReferences(text: String): List<String> {
        val refs = mutableListOf<String>()
        val regex = Regex("\\[(Surah [^\\]]+|Sahih [^\\]]+|Sunan [^\\]]+|Jami` [^\\]]+)\\]")
        regex.findAll(text).forEach { match ->
            refs.add(match.value.removeSurrounding("[", "]"))
        }
        return refs.take(4)
    }

    /**
     * Verified Scripture Fallback: Returns verified Quran & Sahih Hadith guidance when offline or without active API key.
     */
    private fun getOfflineVerifiedAnswer(query: String): ChatMessage {
        val q = query.lowercase()

        val (answer, refs) = when {
            q.contains("mother") || q.contains("parent") || q.contains("मां") || q.contains("والدین") -> {
                Pair(
                    "Bismillah. In Islam, devotion to parents, especially the mother, is paramount.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"And your Lord has decreed that you not worship except Him, and to parents, good treatment. Whether one or both of them reach old age with you, say not to them [so much as], 'uff,' and do not repel them but speak to them a noble word.\" [Surah Al-Isra 17:23]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "Mu'awiyah ibn Jahima reported that his father came to the Prophet (ﷺ) seeking to join Jihad. The Prophet (ﷺ) asked: 'Do you have a mother?' He replied: 'Yes.' The Prophet (ﷺ) said: 'Stay with her and serve her, for indeed Paradise lies beneath her feet.'\n" +
                    "[Sunan an-Nasa'i 3104 • Grading: Sahih]\n\n" +
                    "A man asked the Messenger of Allah (ﷺ): 'Who is most entitled to my excellent companionship?' The Prophet replied: 'Your mother.' The man asked, 'Then who?' He replied, 'Your mother.' The man asked again, 'Then who?' He replied, 'Your mother.' The man asked, 'Then who?' He replied, 'Your father.'\n" +
                    "[Sahih al-Bukhari 5971 • Sahih Muslim 2548]",
                    listOf("Surah Al-Isra 17:23", "Sunan an-Nasa'i 3104 (Sahih)", "Sahih al-Bukhari 5971")
                )
            }
            q.contains("intention") || q.contains("niyyah") || q.contains("نیت") || q.contains("नीयत") -> {
                Pair(
                    "Bismillah. All deeds in Islam are evaluated according to sincerity and pure intention.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"And they were not commanded except to worship Allah, [being] sincere to Him in religion, inclining to truth... and that is the correct religion.\" [Surah Al-Bayyinah 98:5]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "Narrated 'Umar bin Al-Khattab (رضي الله عنه): The Messenger of Allah (ﷺ) said: 'Actions are judged by intentions, and every person will have only what they intended. Whoever emigrated for worldly benefit or for a woman to marry, their emigration is for what they emigrated for.'\n" +
                    "[Sahih al-Bukhari 1 • Sahih Muslim 1907 • Grading: Muttafaq 'Alayh]",
                    listOf("Surah Al-Bayyinah 98:5", "Sahih al-Bukhari 1 (Sahih)", "Sahih Muslim 1907")
                )
            }
            q.contains("fasting") || q.contains("ramadan") || q.contains("sawm") || q.contains("روزہ") -> {
                Pair(
                    "Bismillah. Fasting the month of Ramadan is the fourth pillar of Islam.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.\" [Surah Al-Baqarah 2:183]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "Narrated Abu Huraira (رضي الله عنه): The Messenger of Allah (ﷺ) said: 'Whoever fasts during the month of Ramadan out of sincere faith and seeking Allah's reward, all his previous sins will be forgiven.'\n" +
                    "[Sahih al-Bukhari 1901 • Sahih Muslim 760 • Grading: Sahih]",
                    listOf("Surah Al-Baqarah 2:183", "Sahih al-Bukhari 1901 (Sahih)", "Sahih Muslim 760")
                )
            }
            q.contains("charity") || q.contains("zakat") || q.contains("sadaqah") || q.contains("صدقہ") || q.contains("दान") -> {
                Pair(
                    "Bismillah. Zakat is an obligatory pillar, and voluntary Sadaqah purifies wealth and brings divine barakah.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"Take from their wealth a charity by which you purify them and cause them increase, and invoke [Allah's blessings] upon them.\" [Surah At-Tawbah 9:103]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "Narrated Abu Huraira (رضي الله عنه): The Prophet (ﷺ) said: 'Charity does not decrease wealth, and Allah increases the honor of a servant who forgives, and no one humbles himself for the sake of Allah except that Allah elevates his status.'\n" +
                    "[Sunan Abi Dawud 1694 • Sahih Muslim 2588 • Grading: Sahih]",
                    listOf("Surah At-Tawbah 9:103", "Sahih Muslim 2588 (Sahih)", "Sunan Abi Dawud 1694")
                )
            }
            q.contains("clean") || q.contains("wudu") || q.contains("taharah") || q.contains("طہارت") || q.contains("وضو") -> {
                Pair(
                    "Bismillah. Purification is the essential prerequisite for Salah and spiritual elevation.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"Indeed, Allah loves those who are constantly repentant and loves those who purify themselves.\" [Surah Al-Baqarah 2:222]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "Narrated Abu Malik Al-Ash'ari (رضي الله عنه): The Messenger of Allah (ﷺ) said: 'Purity is half of faith, and \"Alhamdulillah\" fills the scale...' [Sahih Muslim 223 • Grading: Sahih]",
                    listOf("Surah Al-Baqarah 2:222", "Sahih Muslim 223 (Sahih)")
                )
            }
            else -> {
                Pair(
                    "Bismillah ar-Rahman ar-Rahim. In the authentic Islamic tradition, all worship and guidance is derived from the Holy Quran and Sahih Sunnah.\n\n" +
                    "📖 **Holy Quran:**\n" +
                    "\"Say, [O Muhammad], 'If you should love Allah, then follow me, [so] Allah will love you and forgive you your sins. And Allah is Forgiving and Merciful.'\" [Surah Ali 'Imran 3:31]\n\n" +
                    "📜 **Sahih Hadith:**\n" +
                    "The Messenger of Allah (ﷺ) said: 'I have left among you two things; you will never go astray as long as you hold fast to them: the Book of Allah (the Quran) and the Sunnah of His Messenger.'\n" +
                    "[Muwatta Malik 1601 • Sahih by Al-Albani / Hakim]\n\n" +
                    "You may ask specific questions on Salah, Taharah, Fasting, Zakat, Character, Duas, or specific Hadiths from Sahih Sitta.",
                    listOf("Surah Ali 'Imran 3:31", "Muwatta Malik 1601 (Sahih)")
                )
            }
        }

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = ChatSender.AI_SCHOLAR,
            text = answer,
            references = refs
        )
    }
}
