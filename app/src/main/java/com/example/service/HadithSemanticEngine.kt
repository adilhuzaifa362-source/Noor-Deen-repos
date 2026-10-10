package com.example.service

import com.example.model.AppLanguage
import com.example.model.Hadith
import com.example.model.HadithTopic

object HadithSemanticEngine {

    val hadiths: List<Hadith> = listOf(
        Hadith(
            id = "sb_1",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 1",
            chapter = "Book of Revelation (كتاب بدء الوحي)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Muttafaq 'Alayh)",
            narrator = "'Umar bin Al-Khattab",
            sanad = "Narrated 'Umar bin Al-Khattab (رضي الله عنه) from the Messenger of Allah (ﷺ)",
            matnArabic = "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا، فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ",
            matnEn = "Actions are judged by intentions, and every person will have only what they intended. Whoever emigrated for worldly benefit or for a woman to marry, their emigration is for what they emigrated for.",
            matnUr = "اعمال کا دارومدار نیتوں پر ہے اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔ پس جس کی ہجرت دنیا کے حصول یا کسی عورت سے نکاح کے لیے ہو تو اس کی ہجرت اسی چیز کے لیے ہے جس کی طرف اس نے ہجرت کی۔",
            matnHi = "कर्मों का दारोमदार नीयतों पर है, और प्रत्येक व्यक्ति के लिए वही है जिसकी उसने नीयत की। अतः जिसकी हिजरत दुनिया पाने या किसी स्त्री से विवाह के लिए हो, तो उसकी हिजरत उसी के लिए होगी।",
            semanticKeywords = listOf("intention", "intentions", "niyyah", "sincerity", "ikhlas", "purpose", "deeds", "heart", "emigration", "hijrah", "نیت", "اخلاص", "اعمال", "ارادہ", "नीयत", "इरादा")
        ),
        Hadith(
            id = "sb_13",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 13",
            chapter = "Book of Belief (كتاب الإيمان)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            narrator = "Anas bin Malik",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) from the Prophet (ﷺ)",
            matnArabic = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            matnEn = "None of you truly believes until he loves for his brother (in faith and humanity) what he loves for himself.",
            matnUr = "تم میں سے کوئی شخص اس وقت تک سچا مومن نہیں ہو سکتا جب تک کہ وہ اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
            matnHi = "तुम में से कोई व्यक्ति उस समय तक सच्चा मोमिन नहीं हो सकता जब तक कि वह अपने भाई के लिए वही पसंद न करे जो अपने लिए पसंद करता है।",
            semanticKeywords = listOf("brotherhood", "love", "compassion", "empathy", "neighbor", "faith", "humanity", "selfishness", "بھائی چارہ", "محبت", "ہمدردی", "ایمان", "भाईचारा", "प्रेम")
        ),
        Hadith(
            id = "sm_223",
            book = "Sahih Muslim",
            hadithNumber = "Hadith 223",
            chapter = "Book of Purification (كتاب الطهارة)",
            topic = HadithTopic.PRAYER,
            grading = "Sahih",
            narrator = "Abu Malik Al-Ash'ari",
            sanad = "Narrated Abu Malik Al-Ash'ari (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ، وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ أَوْ تَمْلأُ مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ، وَالصَّلاَةُ نُورٌ",
            matnEn = "Cleanliness and purification is half of faith, and 'Alhamdulillah' (Praise be to Allah) fills the scale, and 'SubhanAllah' and 'Alhamdulillah' fill whatever is between heavens and earth, and prayer (Salah) is light.",
            matnUr = "طہارت و پاکیزگی نصف ایمان ہے، اور 'الحمد لله' نیکیوں کے پلڑے کو بھر دیتا ہے، اور 'سبحان الله' اور 'الحمد لله' آسمانوں اور زمین کے درمیانی فاصلے کو بھر دیتے ہیں، اور نماز سراپا نور ہے۔",
            matnHi = "पवित्रता और सफ़ाई आधा ईमान है, और 'अल्हम्दुलिल्लाह' तराज़ू को भर देता है, और 'सुब्हानल्लाह' व 'अल्हम्दुलिल्لاह' आकाश और धरती के बीच की जगह को भर देते हैं, और नमाज़ प्रकाश है।",
            semanticKeywords = listOf("cleanliness", "purity", "taharah", "wudu", "hygiene", "ablution", "purification", "prayer light", "salah", "طہارت", "پاکیزگی", "وضو", "نماز نور", "पवित्रता", "सफ़ाई", "वुज़ू")
        ),
        Hadith(
            id = "st_2003",
            book = "Jami` at-Tirmidhi",
            hadithNumber = "Hadith 2003",
            chapter = "Book of Righteousness and Relations (كتاب البر والصلة)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih (Hasan Sahih)",
            narrator = "Abu Darda",
            sanad = "Narrated Abu Darda (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "مَا شَيْءٌ أَثْقَلُ فِي مِيزَانِ الْمُؤْمِنِ يَوْمَ الْقِيَامَةِ مِنْ خُلُقٍ حَسَنٍ، وَإِنَّ اللَّهَ لَيُبْغِضُ الْفَاحِشَ الْبَذِيءَ",
            matnEn = "Nothing is heavier on the scale of a believer on the Day of Resurrection than good character and noble manners. Indeed, Allah dislikes the foul-mouthed and obscene person.",
            matnUr = "قیامت کے دن مومن کے میزان میں اچھے اخلاق سے زیادہ بھاری کوئی چیز نہیں ہوگی، اور بیشک اللہ تعالی بے ہودہ گو اور بدزبان شخص سے نفرت فرماتا ہے۔",
            matnHi = "क़ियामत के दिन मोमिन के तराज़ू में उत्तम चरित्र (अच्छे अख़लाक़) से भारी कोई चीज़ नहीं होगी, और निस्संदेह अल्लाह अश्लील व अभद्र बात करने वाले से नफ़रत करता है।",
            semanticKeywords = listOf("good character", "akhlaq", "morals", "manners", "adab", "politeness", "etiquette", "foul speech", "swearing", "scale", "حسن اخلاق", "اخلاق", "تہذیب", "بدزبانی", "उत्तम चरित्र", "सदाचार")
        ),
        Hadith(
            id = "sb_1901",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 1901",
            chapter = "Book of Fasting (كتاب الصوم)",
            topic = HadithTopic.FASTING,
            grading = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
            matnEn = "Whoever fasts during the month of Ramadan out of sincere faith and seeking Allah's reward, all his previous sins will be forgiven.",
            matnUr = "جس شخص نے رمضان کے روزے ایمان اور ثواب کی نیت سے رکھے، اس کے پچھلے تمام گناہ معاف کر دیے جاتے ہیں۔",
            matnHi = "जिस व्यक्ति ने रमज़ान के रोज़े ईमान और सवाब की नीयत से रखे, उसके पिछले सारे गुनाह माफ़ कर दिए जाते हैं।",
            semanticKeywords = listOf("fasting", "ramadan", "sawm", "roza", "forgiveness", "maghfirah", "sins", "pardon", "expiation", "reward", "روزہ", "رمضان", "مغفرت", "گناہ معاف", "रोज़ा", "रमज़ान", "क्षमा")
        ),
        Hadith(
            id = "sd_1694",
            book = "Sunan Abi Dawud",
            hadithNumber = "Hadith 1694",
            chapter = "Book of Zakat (كتاب الزكاة)",
            topic = HadithTopic.SADAQAH,
            grading = "Sahih",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) from the Prophet (ﷺ)",
            matnArabic = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا، وَمَا تَوَاضَعَ أَحَدٌ لِلَّهِ إِلَّا رَفَعَهُ اللَّهُ",
            matnEn = "Charity does not decrease wealth, and Allah increases the honor of a person who pardons others, and no one humbles themselves for the sake of Allah except that Allah elevates him.",
            matnUr = "صدقہ کسی مال میں کمی نہیں کرتا، اور معاف کرنے والے کی اللہ صرف عزت ہی بڑھاتا ہے، اور جو کوئی اللہ کی خاطر عاجزی اختیار کرتا ہے اللہ اسے بلندیاں عطا فرماتا ہے۔",
            matnHi = "दान (सदक़ा) माल में कोई कमी नहीं करता, और क्षमा करने वाले का अल्लाह सम्मान ही बढ़ाता है, और जो कोई अल्लाह की ख़ातिर विनम्रता अपनाता है, अल्लाह उसका दर्जा बुलंद फ़रमाता है।",
            semanticKeywords = listOf("charity", "sadaqah", "giving", "zakat", "wealth", "money", "barakah", "forgiveness", "pardon", "humility", "honor", "صدقہ", "زکوٰۃ", "عاجزی", "معافی", "برکت", "दान", "सदक़ा", "विनम्रता")
        ),
        Hadith(
            id = "sn_3104",
            book = "Sunan an-Nasa'i",
            hadithNumber = "Hadith 3104",
            chapter = "Book of Jihad & Good Conduct (كتاب الجهاد)",
            topic = HadithTopic.FAMILY,
            grading = "Hasan Sahih",
            narrator = "Mu'awiyah ibn Jahima",
            sanad = "Narrated Mu'awiyah ibn Jahima that his father came to the Prophet (ﷺ) and said: 'O Messenger of Allah, I wish to strive in jihad and come to consult you.' The Prophet asked: 'Do you have a mother?' He replied: 'Yes.' The Prophet said:",
            matnArabic = "فَالْزَمْهَا، فَإِنَّ الْجَنَّةَ تَحْتَ رِجْلَيْهَا",
            matnEn = "Then stay with her and serve her, for indeed Paradise lies beneath her feet.",
            matnUr = "پس اس کی خدمت میں لگے رہو، کیونکہ بیشک جنت اسی کے قدموں تلے ہے۔",
            matnHi = "तो उसकी सेवा में लगा रह, क्योंकि निस्संदेह जन्नत उसी के क़दमों के नीचे है।",
            semanticKeywords = listOf("mother", "parents", "mother feet", "paradise beneath mother feet", "family", "serving parents", "kindness to mother", "birr al walidayn", "والدین", "ماں", "ماں کی خدمت", "جنت ماں کے قدموں تلے", "माँ", "माता-पिता", "जन्नत")
        ),
        Hadith(
            id = "sim_224",
            book = "Sunan Ibn Majah",
            hadithNumber = "Hadith 224",
            chapter = "The Book of the Sunnah (المقدمة)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Albani)",
            narrator = "Anas bin Malik",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            matnEn = "Seeking beneficial knowledge is an absolute religious obligation upon every Muslim (male and female).",
            matnUr = "دین کا علم حاصل کرنا ہر مسلمان (مرد اور عورت) پر فرض ہے۔",
            matnHi = "ज्ञान प्राप्त करना प्रत्येक मुस्लिम (पुरुष और स्त्री) पर फ़र्ज़ (अनिवार्य) है।",
            semanticKeywords = listOf("knowledge", "ilm", "education", "student", "learning", "study", "scholar", "obligation", "science", "فرض علم", "طلب علم", "تعلیم", "عالم", "ज्ञान", "शिक्षा", "अध्ययन")
        ),
        Hadith(
            id = "sb_6035",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 6035",
            chapter = "Book of Good Manners (كتاب الأدب)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            narrator = "Aisha (Mother of the Believers)",
            sanad = "Narrated Aisha (رضي الله عنها) from the Messenger of Allah (ﷺ)",
            matnArabic = "إِنَّ خِيَارَكُمْ أَحْسَنُكُمْ أَخْلَاقًا، وَلَمْ يَكُنِ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ فَاحِشًا وَلَا مُتَفَحِّشًا",
            matnEn = "The best amongst you are those who have the best character and conduct. The Prophet (ﷺ) was never obscene nor did he ever behave immorally.",
            matnUr = "تم میں سے بہترین لوگ وہ ہیں جن کے اخلاق سب سے اچھے ہیں۔ اور نبی کریم (ﷺ) نہ بدکلام تھے اور نہ بیہودہ بات کرتے تھے۔",
            matnHi = "तुम में से सर्वोत्तम वे हैं जिनका चरित्र सबसे अच्छा है। नबी (ﷺ) न कटुभाषी थे और न ही अभद्र बात करते थे।",
            semanticKeywords = listOf("aisha", "best character", "noble conduct", "gentleness", "speech", "behavior", "عائشہ", "بہترین اخلاق", "حسن خلق", "نرمی", "आयशा", "उत्तम आचरण")
        ),
        Hadith(
            id = "sm_16",
            book = "Sahih Muslim",
            hadithNumber = "Hadith 16",
            chapter = "Book of Faith (كتاب الإيمان)",
            topic = HadithTopic.FAITH,
            grading = "Sahih",
            narrator = "Abdullah ibn Umar",
            sanad = "Narrated Abdullah ibn Umar (رضي الله عنهما) from the Prophet (ﷺ)",
            matnArabic = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَحَجِّ الْبَيْتِ، وَصَوْمِ رَمَضَانَ",
            matnEn = "Islam is built upon five pillars: Testifying that there is no god but Allah and Muhammad is the Messenger of Allah, establishing prayer, paying Zakat, performing Hajj to the House, and fasting Ramadan.",
            matnUr = "اسلام کی بنیاد پانچ ستونوں پر رکھی گئی ہے: اس بات کی گواہی دینا کہ اللہ کے سوا کوئی معبود نہیں اور محمد (ﷺ) اس کے رسول ہیں، نماز قائم کرنا، زکوٰۃ ادا کرنا، بیت اللہ کا حج کرنا، اور رمضان کے روزے رکھنا۔",
            matnHi = "इस्लाम की नींव पांच स्तंभों पर रखी गई है: गवाही देना कि अल्लाह के सिवा कोई पूज्य नहीं और मुहम्मद (ﷺ) उसके रसूल हैं, नमाज़ क़ायم करना, ज़कात देना, हज्ज करना, और रमज़ान के रोज़े रखना।",
            semanticKeywords = listOf("five pillars", "pillars of islam", "arkan al-islam", "shahadah", "tawheed", "ibn umar", "foundations", "ارکان اسلام", "پانچ ستون", "توحید", "بنیاد", "पांच स्तंभ", "अरकान")
        )
    )

    val allNarrators: List<String> = listOf(
        "All Narrators",
        "Abu Huraira",
        "'Umar bin Al-Khattab",
        "Anas bin Malik",
        "Aisha (Mother of the Believers)",
        "Abdullah ibn Umar",
        "Abu Darda",
        "Abu Malik Al-Ash'ari",
        "Mu'awiyah ibn Jahima"
    )

    /**
     * Semantic Search: Evaluates natural language user queries and computes a multi-faceted relevance score.
     */
    fun search(
        query: String,
        selectedTopic: HadithTopic?,
        selectedNarrator: String?,
        selectedLanguage: AppLanguage
    ): List<Hadith> {
        val q = query.trim().lowercase()

        val topicFiltered = hadiths.filter { hadith ->
            val matchTopic = selectedTopic == null || hadith.topic == selectedTopic
            val matchNarrator = selectedNarrator == null ||
                    selectedNarrator == "All Narrators" ||
                    hadith.narrator.contains(selectedNarrator, ignoreCase = true) ||
                    hadith.sanad.contains(selectedNarrator, ignoreCase = true)
            matchTopic && matchNarrator
        }

        if (q.isBlank()) {
            return topicFiltered
        }

        // Tokenize query words
        val queryTokens = q.split("\\s+".toRegex()).filter { it.length > 2 }

        data class ScoredHadith(val hadith: Hadith, val score: Int)

        val scoredList = topicFiltered.map { hadith ->
            var score = 0

            // 1. Exact / Substring in Matn according to language
            if (hadith.matnEn.lowercase().contains(q)) score += 50
            if (hadith.matnUr.contains(q)) score += 50
            if (hadith.matnHi.contains(q)) score += 50
            if (hadith.matnArabic.contains(q)) score += 50
            if (hadith.chapter.lowercase().contains(q)) score += 30
            if (hadith.book.lowercase().contains(q)) score += 20
            if (hadith.narrator.lowercase().contains(q)) score += 40

            // 2. Semantic Keywords match
            hadith.semanticKeywords.forEach { kw ->
                if (q.contains(kw.lowercase()) || kw.lowercase().contains(q)) {
                    score += 45
                }
            }

            // 3. Token overlap match
            queryTokens.forEach { token ->
                if (hadith.matnEn.lowercase().contains(token)) score += 15
                if (hadith.semanticKeywords.any { it.lowercase().contains(token) }) score += 20
                if (hadith.sanad.lowercase().contains(token)) score += 10
            }

            // Natural language intent mapping
            if (q.contains("mother") || q.contains("parent") || q.contains("मां") || q.contains("والدین")) {
                if (hadith.id == "sn_3104") score += 60
            }
            if (q.contains("intention") || q.contains("niyyah") || q.contains("نیت") || q.contains("नीयत")) {
                if (hadith.id == "sb_1") score += 60
            }
            if (q.contains("clean") || q.contains("wudu") || q.contains("وضو") || q.contains("طہارت") || q.contains("सफ़ाई")) {
                if (hadith.id == "sm_223") score += 60
            }
            if (q.contains("character") || q.contains("manner") || q.contains("اخلاق") || q.contains("चरित्र")) {
                if (hadith.topic == HadithTopic.AKHLAQ) score += 40
            }
            if (q.contains("charity") || q.contains("sadaqah") || q.contains("صدقہ") || q.contains("दान")) {
                if (hadith.topic == HadithTopic.SADAQAH) score += 50
            }
            if (q.contains("knowledge") || q.contains("study") || q.contains("علم") || q.contains("ज्ञान")) {
                if (hadith.id == "sim_224") score += 60
            }

            ScoredHadith(hadith, score)
        }

        return scoredList
            .filter { it.score > 0 }
            .sortedByDescending { it.score }
            .map { it.hadith }
            .ifEmpty {
                // If specific semantic scoring had 0 matches, fallback to generic contains on any field
                topicFiltered.filter { h ->
                    h.matnEn.lowercase().contains(q) ||
                    h.matnUr.contains(q) ||
                    h.matnHi.contains(q) ||
                    h.matnArabic.contains(q) ||
                    h.sanad.lowercase().contains(q)
                }
            }
    }
}
