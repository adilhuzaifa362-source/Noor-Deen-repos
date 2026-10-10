package com.example.service

import com.example.data.IslamicData
import com.example.model.Hadith
import com.example.model.HadithTopic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Repository service for fetching and browsing verified Hadith narrations
 * from Sahih Sitta (Sahih al-Bukhari, Sahih Muslim, Sunan Abi Dawud,
 * Jami` at-Tirmidhi, Sunan an-Nasa'i, Sunan Ibn Majah).
 */
object HadithRepository {

    private val _verifiedHadiths = MutableStateFlow<List<Hadith>>(emptyList())
    val verifiedHadiths: StateFlow<List<Hadith>> = _verifiedHadiths.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Canonical collection books of Sahih Sitta
    val sahihSittaBooks = listOf(
        "Sahih al-Bukhari",
        "Sahih Muslim",
        "Jami` at-Tirmidhi",
        "Sunan Abi Dawud",
        "Sunan an-Nasa'i",
        "Sunan Ibn Majah"
    )

    // Expanded verified hadiths dataset across all six canonical books
    private val masterHadithCollection = listOf(
        // Bukhari
        Hadith(
            id = "bukhari_1",
            book = "Sahih al-Bukhari",
            hadithNumber = "Book 1, Hadith 1",
            chapter = "Book of Revelation (كتاب بدء الوحي)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Muttafaq 'Alayh)",
            narrator = "'Umar bin Al-Khattab",
            sanad = "Narrated 'Umar bin Al-Khattab (رضي الله عنه) from the Messenger of Allah (ﷺ)",
            matnArabic = "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا، فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ",
            matnEn = "Actions are judged by intentions, and every person will have only what they intended. Whoever emigrated for worldly benefit or for a woman to marry, their emigration is for what they emigrated for.",
            matnUr = "اعمال کا دارومدار نیتوں پر ہے اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔ پس جس کی ہجرت دنیا کے حصول یا کسی عورت سے نکاح کے لیے ہو تو اس کی ہجرت اسی چیز کے لیے ہے جس کی طرف اس نے ہجرت کی۔",
            matnHi = "कर्मों का दारोमदार नीयतों पर है, और प्रत्येक व्यक्ति के लिए वही है जिसकी उसने नीयत की। अतः जिसकी हिजरत दुनिया पाने या किसी स्त्री से विवाह के लिए हो, तो उसकी हिजरत उसी के लिए होगी।",
            semanticKeywords = listOf("niyyah", "intention", "ikhlas", "sincerity", "actions", "amal", "reward")
        ),
        Hadith(
            id = "bukhari_13",
            book = "Sahih al-Bukhari",
            hadithNumber = "Book 2, Hadith 13",
            chapter = "Book of Belief (كتاب الإيمان)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            narrator = "Anas bin Malik",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            matnEn = "None of you truly believes until he loves for his brother (in faith & humanity) what he loves for himself.",
            matnUr = "تم میں سے کوئی شخص اس وقت تک سچا مومن نہیں ہو سکتا جب تک کہ وہ اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
            matnHi = "तुम में से कोई व्यक्ति उस समय तक सच्चा मोमिन नहीं हो सकता जब तक कि वह अपने भाई के लिए वही पसंद न करे जो अपने लिए पसंद करता है।",
            semanticKeywords = listOf("brotherhood", "love", "iman", "faith", "empathy", "golden rule")
        ),
        Hadith(
            id = "bukhari_5027",
            book = "Sahih al-Bukhari",
            hadithNumber = "Book 66, Hadith 49",
            chapter = "Virtues of the Quran (كتاب فضائل القرآن)",
            topic = HadithTopic.FAITH,
            grading = "Sahih",
            narrator = "'Uthman bin 'Affan",
            sanad = "Narrated 'Uthman bin 'Affan (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
            matnEn = "The best among you are those who learn the Quran and teach it to others.",
            matnUr = "تم میں سے بہترین شخص وہ ہے جو قرآن سیکھے اور اسے دوسروں کو سکھائے۔",
            matnHi = "तुम में सबसे उत्तम व्यक्ति वह है जो क़ुरआन सीखे और दूसरों को सिखाए।",
            semanticKeywords = listOf("quran", "recitation", "teaching", "learning", "knowledge", "virtue")
        ),
        Hadith(
            id = "bukhari_1901",
            book = "Sahih al-Bukhari",
            hadithNumber = "Book 30, Hadith 11",
            chapter = "Book of Fasting (كتاب الصوم)",
            topic = HadithTopic.FASTING,
            grading = "Sahih (Muttafaq 'Alayh)",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
            matnEn = "Whoever fasts during the month of Ramadan out of sincere faith and seeking Allah's reward, all his previous sins will be forgiven.",
            matnUr = "جس شخص نے رمضان کے روزے ایمان اور ثواب کی نیت سے رکھے، اس کے پچھلے تمام گناہ معاف کر دیے جاتے ہیں۔",
            matnHi = "जिस व्यक्ति ने रमज़ान के रोज़े ईमान और सवाब की नीयत से रखे, उसके पिछले सारे गुनाह माफ़ कर दिए जाते हैं।",
            semanticKeywords = listOf("ramadan", "sawm", "fasting", "forgiveness", "maghfirah", "reward")
        ),
        // Muslim
        Hadith(
            id = "muslim_223",
            book = "Sahih Muslim",
            hadithNumber = "Book 2, Hadith 1",
            chapter = "Book of Purification (كتاب الطهارة)",
            topic = HadithTopic.PRAYER,
            grading = "Sahih",
            narrator = "Abu Malik Al-Ash'ari",
            sanad = "Narrated Abu Malik Al-Ash'ari (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ، وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ أَوْ تَمْلأُ مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ، وَالصَّلاَةُ نُورٌ",
            matnEn = "Cleanliness and purification is half of faith, and 'Alhamdulillah' fills the scale, and 'SubhanAllah' and 'Alhamdulillah' fill between heaven and earth, and prayer is light.",
            matnUr = "طہارت و پاکیزگی نصف ایمان ہے، اور 'الحمد لله' نیکیوں کے پلڑے کو بھر دیتا ہے، اور 'سبحان الله' اور 'الحمد لله' آسمانوں اور زمین کے درمیانی فاصلے کو بھر دیتے ہیں، اور نماز سراپا نور ہے۔",
            matnHi = "पवित्रता और सफ़ाई आधा ईमान है, और 'अल्हम्दुलिल्लाह' तराज़ू को भर देता है, और 'सुब्हानल्लाह' व 'अल्हम्दुलिल्लाह' आकाश और धरती के बीच को भर देते हैं, और नमाज़ प्रकाश है।",
            semanticKeywords = listOf("taharah", "wudu", "purification", "dhikr", "salah", "prayer", "light")
        ),
        Hadith(
            id = "muslim_2564",
            book = "Sahih Muslim",
            hadithNumber = "Book 45, Hadith 32",
            chapter = "Book of Virtue & Good Manners (كتاب البر والصلة)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "إِنَّ اللَّهَ لَا يَنْظُرُ إِلَى صُوَرِكُمْ وَأَمْوَالِكُمْ، وَلَكِنْ يَنْظُرُ إِلَى قُلُوبِكُمْ وَأَعْمَالِكُمْ",
            matnEn = "Verily, Allah does not look at your outward appearances or your wealth, but He looks into your hearts and your deeds.",
            matnUr = "بیشک اللہ تعالی تمہاری صورتوں اور تمہارے مالوں کو نہیں دیکھتا، بلکہ وہ تمہارے دلوں اور تمہارے اعمال کو دیکھتا ہے۔",
            matnHi = "निस्संदेह अल्लाह तुम्हारी सूरतों और तुम्हारी दौलत को नहीं देखता, बल्कि वह तुम्हारे दिलों और तुम्हारे कर्मों को देखता है।",
            semanticKeywords = listOf("heart", "qalb", "appearance", "wealth", "piety", "taqwa", "sincerity")
        ),
        Hadith(
            id = "muslim_2699",
            book = "Sahih Muslim",
            hadithNumber = "Book 48, Hadith 38",
            chapter = "Book of Remembrance & Dua (كتاب الذكر والدعاء)",
            topic = HadithTopic.FAITH,
            grading = "Sahih",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "مَنْ سَلَكَ طَرِيقًا يَلْتَمِسُ فِيهِ عِلْمًا، سَهَّلَ اللَّهُ لَهُ بِهِ طَرِيقًا إِلَى الْجَنَّةِ",
            matnEn = "Whoever travels a path in search of sacred knowledge, Allah will make easy for him by it a path to Paradise.",
            matnUr = "جو شخص علم کی تلاش میں کسی راستے پر چلتا ہے، اللہ تعالی اس کے بدلے اس کے لیے جنت کا راستہ آسان فرما دیتا ہے۔",
            matnHi = "जो व्यक्ति ज्ञान की खोज में किसी मार्ग पर चलता है, अल्लाह उसके बदले उसके लिए जन्नत का मार्ग आसान कर देता है।",
            semanticKeywords = listOf("ilm", "knowledge", "seeking knowledge", "jannah", "paradise", "scholar")
        ),
        // Tirmidhi
        Hadith(
            id = "tirmidhi_2003",
            book = "Jami` at-Tirmidhi",
            hadithNumber = "Book 27, Hadith 109",
            chapter = "Book of Righteousness and Relations (كتاب البر والصلة)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih (Hasan Sahih)",
            narrator = "Abu Darda",
            sanad = "Narrated Abu Darda (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "مَا شَيْءٌ أَثْقَلُ فِي مِيزَانِ الْمُؤْمِنِ يَوْمَ الْقِيَامَةِ مِنْ خُلُقٍ حَسَنٍ، وَإِنَّ اللَّهَ لَيُبْغِضُ الْفَاحِشَ الْبَذِيءَ",
            matnEn = "Nothing is heavier on the scale of a believer on the Day of Resurrection than good character and noble manners. Indeed, Allah dislikes the foul-mouthed and obscene person.",
            matnUr = "قیامت کے دن مومن کے میزان میں اچھے اخلاق سے زیادہ بھاری کوئی چیز نہیں ہوگی، اور بیشک اللہ تعالی بے ہودہ گو اور بدزبان شخص سے نفرت فرماتا ہے۔",
            matnHi = "क़ियामत के दिन मोमिन के तराज़ू में उत्तम चरित्र (अच्छे अख़लाक़) से भारी कोई चीज़ नहीं होगी, और निस्संदेह अल्लाह अश्लील व अभद्र बात करने वाले से नफ़रत करता है।",
            semanticKeywords = listOf("akhlaq", "character", "manners", "mizan", "scales", "speech")
        ),
        Hadith(
            id = "tirmidhi_1956",
            book = "Jami` at-Tirmidhi",
            hadithNumber = "Book 27, Hadith 62",
            chapter = "Book of Righteousness (كتاب البر والصلة)",
            topic = HadithTopic.SADAQAH,
            grading = "Sahih",
            narrator = "Abu Dharr",
            sanad = "Narrated Abu Dharr (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
            matnEn = "Your smiling in the face of your brother is charity for you.",
            matnUr = "اپنے بھائی کے سامنے مسکرانا بھی تمہارے لیے ایک صدقہ ہے۔",
            matnHi = "अपने भाई के सामने तुम्हारा मुस्कुराना भी तुम्हारे लिए एक सदक़ा (दान) है।",
            semanticKeywords = listOf("smile", "charity", "sadaqah", "kindness", "gentleness", "brotherhood")
        ),
        // Abu Dawud
        Hadith(
            id = "abudawud_1694",
            book = "Sunan Abi Dawud",
            hadithNumber = "Book 9, Hadith 139",
            chapter = "Book of Zakat (كتاب الزكاة)",
            topic = HadithTopic.SADAQAH,
            grading = "Sahih",
            narrator = "Abu Huraira",
            sanad = "Narrated Abu Huraira (رضي الله عنه) from the Prophet (ﷺ)",
            matnArabic = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا، وَمَا تَوَاضَعَ أَحَدٌ لِلَّهِ إِلَّا رَفَعَهُ اللَّهُ",
            matnEn = "Charity does not decrease wealth, and Allah increases the honor of a person who pardons others, and no one humbles themselves for the sake of Allah except that Allah elevates him.",
            matnUr = "صدقہ کسی مال میں کمی نہیں کرتا، اور معاف کرنے والے کی اللہ صرف عزت ہی بڑھاتا ہے، اور جو کوئی اللہ کی خاطر عاجزی اختیار کرتا ہے اللہ اسے بلندیاں عطا فرماتا ہے۔",
            matnHi = "दान (सदक़ा) माल में कोई कमी नहीं करता, और क्षमा करने वाले का अल्लाह सम्मान ही बढ़ाता है, और जो कोई अल्लाह की ख़ातिर विनम्रता अपनाता है, अल्लाह उसका दर्जा बुलंद फ़रमाता है।",
            semanticKeywords = listOf("zakat", "sadaqah", "forgiveness", "humility", "honor", "wealth")
        ),
        Hadith(
            id = "abudawud_4941",
            book = "Sunan Abi Dawud",
            hadithNumber = "Book 43, Hadith 169",
            chapter = "Book of General Behavior (كتاب الأدب)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            narrator = "Abdullah ibn Amr",
            sanad = "Narrated Abdullah ibn Amr (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمَنُ، ارْحَمُوا مَنْ فِي الْأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ",
            matnEn = "Those who show mercy will be shown mercy by the Most Merciful. Show mercy to those who are on earth, and He who is in heaven will show mercy to you.",
            matnUr = "رحم کرنے والوں پر رحمن رحم فرماتا ہے۔ تم زمین والوں پر رحم کرو، آسمان والا تم پر رحم فرمائے گا۔",
            matnHi = "दया करने वालों पर दयालु अल्लाह दया करता है। तुम धरती वालों पर दया करो, आकाश वाला तुम पर दया करेगा।",
            semanticKeywords = listOf("mercy", "compassion", "rahmah", "kindness", "gentleness")
        ),
        // Nasa'i
        Hadith(
            id = "nasai_3104",
            book = "Sunan an-Nasa'i",
            hadithNumber = "Book 25, Hadith 20",
            chapter = "Book of Jihad (كتاب الجهاد)",
            topic = HadithTopic.FAMILY,
            grading = "Hasan Sahih",
            narrator = "Mu'awiyah ibn Jahima",
            sanad = "Narrated Mu'awiyah ibn Jahima that his father came seeking jihad, so the Prophet (ﷺ) asked: 'Do you have a mother?' He said: 'Yes.' The Prophet said:",
            matnArabic = "فَالْزَمْهَا، فَإِنَّ الْجَنَّةَ تَحْتَ رِجْلَيْهَا",
            matnEn = "Then stay with her and serve her faithfully, for indeed Paradise lies beneath her feet.",
            matnUr = "پس اس کی خدمت میں لگے رہو، کیونکہ بیشک جنت اسی کے قدموں تلے ہے۔",
            matnHi = "तो उसकी सेवा में लगा रह, क्योंकि निस्संदेह जन्नत उसी के क़दमों के नीचे है।",
            semanticKeywords = listOf("mother", "parents", "paradise", "jannah", "family", "obedience", "respect")
        ),
        Hadith(
            id = "nasai_1298",
            book = "Sunan an-Nasa'i",
            hadithNumber = "Book 13, Hadith 119",
            chapter = "Book of Forgetfulness in Prayer (كتاب السهو)",
            topic = HadithTopic.PRAYER,
            grading = "Sahih",
            narrator = "Anas bin Malik",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "مَنْ صَلَّى عَلَيَّ صَلَاةً وَاحِدَةً صَلَّى اللَّهُ عَلَيْهِ عَشْرَ صَلَوَاتٍ، وَحُطَّتْ عَنْهُ عَشْرُ خَطِيئَاتٍ، وَرُفِعَتْ لَهُ عَشْرُ دَرَجَاتٍ",
            matnEn = "Whoever sends blessings upon me once, Allah will send blessings upon him ten times, erase ten of his sins, and raise his status ten degrees.",
            matnUr = "جو مجھ پر ایک بار درود بھیجے گا، اللہ اس پر دس رحمتیں نازل فرمائے گا، اس کے دس گناہ مٹا دے گا اور اس کے دس درجات بلند فرمائے گا۔",
            matnHi = "जो मुझ पर एक बार दुरूद भेजेगा, अल्लाह उस पर दस रहमतें नाज़िल फ़रमाएगा, उसके दस गुनाह मिटा देगा और उसके दस दर्जे बुलंद करेगा।",
            semanticKeywords = listOf("durood", "salawat", "blessings", "prophet", "forgiveness", "ranks")
        ),
        // Ibn Majah
        Hadith(
            id = "ibnmajah_224",
            book = "Sunan Ibn Majah",
            hadithNumber = "Book 1, Hadith 224",
            chapter = "The Book of the Sunnah (المقدمة)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Albani)",
            narrator = "Anas bin Malik",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            matnEn = "Seeking sacred knowledge is an absolute obligation upon every Muslim (male and female).",
            matnUr = "دین کا علم حاصل کرنا ہر مسلمان (مرد اور عورت) پر فرض ہے۔",
            matnHi = "ज्ञान प्राप्त करना प्रत्येक मुस्लिम (पुरुष और स्त्री) पर फ़र्ज़ (अनिवार्य) है।",
            semanticKeywords = listOf("knowledge", "obligation", "ilm", "study", "learning", "duty")
        ),
        Hadith(
            id = "ibnmajah_4250",
            book = "Sunan Ibn Majah",
            hadithNumber = "Book 37, Hadith 151",
            chapter = "Book of Asceticism / Zuhd (كتاب الزهد)",
            topic = HadithTopic.FAITH,
            grading = "Sahih",
            narrator = "Abdullah ibn Mas'ud",
            sanad = "Narrated Abdullah ibn Mas'ud (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "التَّائِبُ مِنَ الذَّنْبِ كَمَنْ لَا ذَنْبَ لَهُ",
            matnEn = "The one who repents from sin is like one who has no sin at all.",
            matnUr = "گناہ سے توبہ کرنے والا ایسا ہے جیسے اس نے کوئی گناہ کیا ہی نہ ہو۔",
            matnHi = "गुनाह से तौबा करने वाला ऐसा है जैसे उसने कोई गुनाह किया ही न हो।",
            semanticKeywords = listOf("tawbah", "repentance", "forgiveness", "clean slate", "mercy", "istighfar")
        )
    )

    init {
        _verifiedHadiths.value = masterHadithCollection
    }

    /**
     * Get all verified narrations from the repository.
     */
    fun getAllHadiths(): List<Hadith> = _verifiedHadiths.value

    /**
     * Fetch narrations filtered by canonical collection book.
     */
    fun getHadithsByBook(book: String): List<Hadith> {
        return _verifiedHadiths.value.filter { it.book.equals(book, ignoreCase = true) }
    }

    /**
     * Fetch narrations filtered by topic/category.
     */
    fun getHadithsByCategory(topic: HadithTopic): List<Hadith> {
        return _verifiedHadiths.value.filter { it.topic == topic }
    }

    /**
     * Get the verified Hadith of the Day.
     */
    fun getDailyHadith(): Hadith {
        val all = _verifiedHadiths.value
        if (all.isEmpty()) return masterHadithCollection.first()
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return all[dayOfYear % all.size]
    }

    /**
     * Search and filter narrations with book, category, and narrator criteria.
     */
    suspend fun searchNarrations(
        query: String = "",
        bookFilter: String? = null,
        topicFilter: HadithTopic? = null,
        narratorFilter: String? = null
    ): List<Hadith> = withContext(Dispatchers.Default) {
        var result = _verifiedHadiths.value

        if (!bookFilter.isNullOrBlank() && bookFilter != "All Books") {
            result = result.filter { it.book.equals(bookFilter, ignoreCase = true) }
        }

        if (topicFilter != null) {
            result = result.filter { it.topic == topicFilter }
        }

        if (!narratorFilter.isNullOrBlank() && narratorFilter != "All Narrators") {
            result = result.filter { it.narrator.contains(narratorFilter, ignoreCase = true) || it.sanad.contains(narratorFilter, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter { hadith ->
                hadith.matnEn.lowercase().contains(q) ||
                hadith.matnArabic.contains(q) ||
                hadith.matnUr.contains(q) ||
                hadith.matnHi.contains(q) ||
                hadith.chapter.lowercase().contains(q) ||
                hadith.hadithNumber.lowercase().contains(q) ||
                hadith.semanticKeywords.any { it.contains(q) }
            }
        }

        result
    }

    /**
     * Asynchronous fetch simulation that updates repository cache with verified narrations.
     */
    suspend fun refreshRepository(): Result<Int> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        try {
            // Emulate quick network verification & guarantee fallback
            kotlinx.coroutines.delay(400)
            _verifiedHadiths.value = masterHadithCollection
            Result.success(masterHadithCollection.size)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }
}
