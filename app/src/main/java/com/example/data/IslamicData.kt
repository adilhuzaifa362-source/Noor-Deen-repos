package com.example.data

import com.example.model.AsmaName
import com.example.model.CityLocation
import com.example.model.DuaCategory
import com.example.model.Hadith
import com.example.model.HadithTopic
import com.example.model.HijriDateEvent
import com.example.model.MasjidJamaat
import com.example.model.Qari
import com.example.model.RevelationType
import com.example.model.Supplication
import com.example.model.Surah
import com.example.model.TajweedSpan
import com.example.model.TajweedType
import com.example.model.Verse

object IslamicData {

    val qaris = listOf(
        Qari("mishary", "Sheikh Mishary Rashid Alafasy", "Kuwait", "Murattal / Hafs"),
        Qari("abdulbasit", "Sheikh Abdul Basit Abdul Samad", "Egypt", "Mujawwad / Classic"),
        Qari("sudais", "Sheikh Abdur-Rahman As-Sudais", "Saudi Arabia", "Haramain Makkah"),
        Qari("shuraym", "Sheikh Saud Ash-Shuraym", "Saudi Arabia", "Haramain Makkah"),
        Qari("rifai", "Sheikh Hani Ar-Rifai", "Saudi Arabia", "Khushoo' / Emotive"),
        Qari("muaiqly", "Sheikh Maher Al-Muaiqly", "Saudi Arabia", "Haramain Makkah")
    )

    // Regional countries for South Asia and neighboring nations
    val regionalCountries = listOf(
        "India",
        "Pakistan",
        "Bangladesh",
        "Afghanistan",
        "Nepal",
        "Sri Lanka",
        "Maldives",
        "Bhutan",
        "Myanmar"
    )

    // Curated cities personalized exclusively for India and its neighboring countries
    val worldCities = listOf(
        // India (Default Region)
        CityLocation("Delhi (New Delhi)", "India", 28.6139, 77.2090, 5.5),
        CityLocation("Mumbai", "India", 19.0760, 72.8777, 5.5),
        CityLocation("Hyderabad", "India", 17.3850, 78.4867, 5.5),
        CityLocation("Kolkata", "India", 22.5726, 88.3639, 5.5),
        CityLocation("Bengaluru", "India", 12.9716, 77.5946, 5.5),
        CityLocation("Chennai", "India", 13.0827, 80.2707, 5.5),
        CityLocation("Lucknow", "India", 26.8467, 80.9462, 5.5),
        CityLocation("Srinagar (Kashmir)", "India", 34.0837, 74.7973, 5.5),
        CityLocation("Ahmedabad", "India", 23.0225, 72.5714, 5.5),
        CityLocation("Patna", "India", 25.5941, 85.1376, 5.5),
        CityLocation("Bhopal", "India", 23.2599, 77.4126, 5.5),
        CityLocation("Kozhikode (Calicut)", "India", 11.2588, 75.7804, 5.5),
        CityLocation("Jaipur", "India", 26.9124, 75.7873, 5.5),
        CityLocation("Surat", "India", 21.1702, 72.8311, 5.5),
        CityLocation("Deoband", "India", 29.6953, 77.6766, 5.5),
        CityLocation("Aligarh", "India", 27.8974, 78.0880, 5.5),
        CityLocation("Pune", "India", 18.5204, 73.8567, 5.5),
        CityLocation("Guwahati (Assam)", "India", 26.1445, 91.7362, 5.5),
        CityLocation("Ranchi", "India", 23.3441, 85.3096, 5.5),
        CityLocation("Varanasi", "India", 25.3176, 82.9739, 5.5),

        // Pakistan
        CityLocation("Karachi", "Pakistan", 24.8607, 67.0011, 5.0),
        CityLocation("Lahore", "Pakistan", 31.5497, 74.3436, 5.0),
        CityLocation("Islamabad", "Pakistan", 33.6844, 73.0479, 5.0),
        CityLocation("Rawalpindi", "Pakistan", 33.5651, 73.0169, 5.0),
        CityLocation("Peshawar", "Pakistan", 34.0151, 71.5249, 5.0),
        CityLocation("Quetta", "Pakistan", 30.1798, 66.9750, 5.0),
        CityLocation("Multan", "Pakistan", 30.1575, 71.5249, 5.0),
        CityLocation("Faisalabad", "Pakistan", 31.4504, 73.1350, 5.0),
        CityLocation("Sialkot", "Pakistan", 32.4945, 74.5229, 5.0),

        // Bangladesh
        CityLocation("Dhaka", "Bangladesh", 23.8103, 90.4125, 6.0),
        CityLocation("Chittagong (Chattogram)", "Bangladesh", 22.3569, 91.7832, 6.0),
        CityLocation("Sylhet", "Bangladesh", 24.8949, 91.8687, 6.0),
        CityLocation("Rajshahi", "Bangladesh", 24.3636, 88.6241, 6.0),
        CityLocation("Khulna", "Bangladesh", 22.8456, 89.5403, 6.0),
        CityLocation("Cox's Bazar", "Bangladesh", 21.4272, 92.0058, 6.0),
        CityLocation("Barisal", "Bangladesh", 22.7010, 90.3535, 6.0),

        // Afghanistan
        CityLocation("Kabul", "Afghanistan", 34.5553, 69.2075, 4.5),
        CityLocation("Kandahar", "Afghanistan", 31.6289, 65.7372, 4.5),
        CityLocation("Herat", "Afghanistan", 34.3529, 62.2040, 4.5),
        CityLocation("Mazar-i-Sharif", "Afghanistan", 36.7061, 67.1123, 4.5),
        CityLocation("Jalalabad", "Afghanistan", 34.4254, 70.4586, 4.5),

        // Nepal
        CityLocation("Kathmandu", "Nepal", 27.7172, 85.3240, 5.75),
        CityLocation("Birgunj", "Nepal", 27.0128, 84.8774, 5.75),
        CityLocation("Nepalgunj", "Nepal", 28.0500, 81.6167, 5.75),
        CityLocation("Pokhara", "Nepal", 28.2096, 83.9856, 5.75),

        // Sri Lanka
        CityLocation("Colombo", "Sri Lanka", 6.9271, 79.8612, 5.5),
        CityLocation("Kandy", "Sri Lanka", 7.2906, 80.6337, 5.5),
        CityLocation("Galle", "Sri Lanka", 6.0535, 80.2210, 5.5),
        CityLocation("Beruwala", "Sri Lanka", 6.4789, 79.9828, 5.5),

        // Maldives
        CityLocation("Malé", "Maldives", 4.1755, 73.5093, 5.0),
        CityLocation("Addu City", "Maldives", -0.6304, 73.1583, 5.0),

        // Bhutan
        CityLocation("Thimphu", "Bhutan", 27.4728, 89.6393, 6.0),
        CityLocation("Phuntsholing", "Bhutan", 26.8523, 89.3842, 6.0),

        // Myanmar
        CityLocation("Yangon", "Myanmar", 16.8661, 96.1951, 6.5),
        CityLocation("Mandalay", "Myanmar", 21.9588, 96.0891, 6.5),
        CityLocation("Sittwe (Rakhine)", "Myanmar", 20.1462, 92.8984, 6.5),
        CityLocation("Mawlamyine", "Myanmar", 16.4905, 97.6283, 6.5)
    )

    val sampleMasjids = listOf(
        MasjidJamaat(
            id = "m1",
            name = "Jama Masjid (Delhi)",
            distanceKm = 0.5,
            address = "Chandni Chowk, Old Delhi, India",
            fajrAzan = "05:00", fajrJamaat = "05:25",
            dhuhrAzan = "12:30", dhuhrJamaat = "12:50",
            asrAzan = "16:15", asrJamaat = "16:30",
            maghribAzan = "18:22", maghribJamaat = "18:27",
            ishaAzan = "19:45", ishaJamaat = "20:05",
            lastVerifiedBy = "Imam Bukhari Office (Verified Today)"
        ),
        MasjidJamaat(
            id = "m2",
            name = "Charminar Mecca Masjid (Hyderabad)",
            distanceKm = 1.2,
            address = "Near Charminar, Hyderabad, India",
            fajrAzan = "05:05", fajrJamaat = "05:30",
            dhuhrAzan = "12:30", dhuhrJamaat = "13:00",
            asrAzan = "16:20", asrJamaat = "16:35",
            maghribAzan = "18:22", maghribJamaat = "18:28",
            ishaAzan = "19:50", ishaJamaat = "20:15",
            lastVerifiedBy = "Masjid Committee (Verified Today)"
        ),
        MasjidJamaat(
            id = "m3",
            name = "Taj-ul-Masajid (Bhopal)",
            distanceKm = 2.0,
            address = "Kohefiza, Bhopal, India",
            fajrAzan = "05:00", fajrJamaat = "05:20",
            dhuhrAzan = "12:30", dhuhrJamaat = "12:45",
            asrAzan = "16:15", asrJamaat = "16:30",
            maghribAzan = "18:22", maghribJamaat = "18:26",
            ishaAzan = "19:40", ishaJamaat = "20:00",
            lastVerifiedBy = "Darul Uloom Taj-ul-Masajid (Verified Yesterday)"
        ),
        MasjidJamaat(
            id = "m4",
            name = "Badshahi Mosque (Lahore)",
            distanceKm = 1.8,
            address = "Walled City, Lahore, Pakistan",
            fajrAzan = "05:05", fajrJamaat = "05:30",
            dhuhrAzan = "12:30", dhuhrJamaat = "13:00",
            asrAzan = "16:20", asrJamaat = "16:35",
            maghribAzan = "18:20", maghribJamaat = "18:25",
            ishaAzan = "19:50", ishaJamaat = "20:15",
            lastVerifiedBy = "Auqaf Department (Verified Today)"
        ),
        MasjidJamaat(
            id = "m5",
            name = "Baitul Mukarram National Mosque (Dhaka)",
            distanceKm = 1.5,
            address = "Paltan, Dhaka, Bangladesh",
            fajrAzan = "04:55", fajrJamaat = "05:20",
            dhuhrAzan = "12:15", dhuhrJamaat = "12:45",
            asrAzan = "16:10", asrJamaat = "16:25",
            maghribAzan = "18:15", maghribJamaat = "18:20",
            ishaAzan = "19:40", ishaJamaat = "20:00",
            lastVerifiedBy = "Islamic Foundation Bangladesh"
        ),
        MasjidJamaat(
            id = "m6",
            name = "Jami Ul-Alfar Red Mosque (Colombo)",
            distanceKm = 2.4,
            address = "Pettah, Colombo, Sri Lanka",
            fajrAzan = "05:00", fajrJamaat = "05:25",
            dhuhrAzan = "12:20", dhuhrJamaat = "12:45",
            asrAzan = "16:15", asrJamaat = "16:30",
            maghribAzan = "18:18", maghribJamaat = "18:23",
            ishaAzan = "19:35", ishaJamaat = "19:55",
            lastVerifiedBy = "Trustee Board (Verified Today)"
        )
    )

    // Detailed reader Surahs
    val fatihaVerses = listOf(
        Verse(
            surahNumber = 1,
            ayahNumber = 1,
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            transliteration = "Bismillāhir-Raḥmānir-Raḥīm",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
            translationUr = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔",
            translationHi = "अल्लाह के नाम से जो बड़ा कृपालु, अत्यंत दयावान है।",
            tajweedSpans = listOf(
                TajweedSpan(18, 24, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 2,
            arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            transliteration = "Al-ḥamdu lillāhi Rabbil-‘ālamīn",
            translationEn = "[All] praise is [due] to Allah, Lord of the worlds -",
            translationUr = "سب تعریف اللہ ہی کے لیے ہے جو تمام جہانوں کا پالنے والا ہے۔",
            translationHi = "सब प्रशंसा अल्लाह ही के लिए है जो सारे संसार का पालनहार है।",
            tajweedSpans = listOf(
                TajweedSpan(22, 28, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 3,
            arabicText = "الرَّحْمَٰنِ الرَّحِيمِ",
            transliteration = "Ar-Raḥmānir-Raḥīm",
            translationEn = "The Entirely Merciful, the Especially Merciful,",
            translationUr = "بہت مہربان نہایت رحم فرمانے والا۔",
            translationHi = "बड़ा कृपालु, अत्यंत दयावान है।",
            tajweedSpans = listOf(
                TajweedSpan(13, 19, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 4,
            arabicText = "مَالِكِ يَوْمِ الدِّينِ",
            transliteration = "Māliki Yawmid-Dīn",
            translationEn = "Sovereign of the Day of Recompense.",
            translationUr = "بدلے کے دن (روزِ جزا) کا مالک۔",
            translationHi = "बदले के दिन (क़ियामत) का मालिक है।",
            tajweedSpans = listOf(
                TajweedSpan(14, 20, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 5,
            arabicText = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            transliteration = "Iyyāka na‘budu wa iyyāka nasta‘īn",
            translationEn = "It is You we worship and You we ask for help.",
            translationUr = "ہم صرف تیری ہی عبادت کرتے ہیں اور صرف تجھ ہی سے مدد مانگتے ہیں۔",
            translationHi = "हम तेरी ही इबादत करते हैं और तुझी से मदद चाहते हैं।",
            tajweedSpans = listOf(
                TajweedSpan(26, 32, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 6,
            arabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            transliteration = "Ihdinaṣ-ṣirāṭal-mustaqīm",
            translationEn = "Guide us to the straight path -",
            translationUr = "ہمیں سیدھے راستے کی ہدایت فرما۔",
            translationHi = "हमें सीधा रास्ता दिखा।",
            tajweedSpans = listOf(
                TajweedSpan(23, 29, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 1,
            ayahNumber = 7,
            arabicText = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            transliteration = "Ṣirāṭalladhīna an‘amta ‘alayhim ghayril-maghḍūbi ‘alayhim wa laḍ-ḍāllīn",
            translationEn = "The path of those upon whom You have bestowed favor, not of those who have earned Your anger or of those who are astray.",
            translationUr = "ان لوگوں کا راستہ جن پر تو نے انعام فرمایا، جن پر نہ غضب ہوا اور نہ وہ گمراہ ہوئے۔",
            translationHi = "उन लोगों का मार्ग जिन पर तूने कृपा की, न कि उनका जिन पर तेरा क्रोध हुआ और न पथभ्रष्टों का।",
            tajweedSpans = listOf(
                TajweedSpan(16, 22, TajweedType.IKHFA),
                TajweedSpan(62, 70, TajweedType.MADD)
            )
        )
    )

    val ikhlasVerses = listOf(
        Verse(
            surahNumber = 112,
            ayahNumber = 1,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            transliteration = "Qul Huwallāhu Aḥad",
            translationEn = "Say, \"He is Allah, [who is] One,",
            translationUr = "آپ فرما دیجیے کہ وہ اللہ ایک ہے۔",
            translationHi = "कह दो, \"वह अल्लाह एक है,\"",
            tajweedSpans = listOf(
                TajweedSpan(16, 20, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 112,
            ayahNumber = 2,
            arabicText = "اللَّهُ الصَّمَدُ",
            transliteration = "Allāhuṣ-Ṣamad",
            translationEn = "Allah, the Eternal Refuge.",
            translationUr = "اللہ بے نیاز ہے (سب اس کے محتاج ہیں)۔",
            translationHi = "अल्लाह बेनियाज़ (सर्वथा आत्मनिर्भर) है।",
            tajweedSpans = listOf(
                TajweedSpan(10, 14, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 112,
            ayahNumber = 3,
            arabicText = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            transliteration = "Lam yalid wa lam yūlad",
            translationEn = "He neither begets nor is born,",
            translationUr = "نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے،",
            translationHi = "न वह किसी का पिता है और न किसी का बेटा,",
            tajweedSpans = listOf(
                TajweedSpan(7, 10, TajweedType.QALQALAH),
                TajweedSpan(18, 22, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 112,
            ayahNumber = 4,
            arabicText = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            transliteration = "Wa lam yakul-lahu kufuwan aḥad",
            translationEn = "Nor is there to Him any equivalent.\"",
            translationUr = "اور نہ کوئی اس کا ہمسر (برابر) ہے۔",
            translationHi = "और न कोई उसके बराबर का है।\"",
            tajweedSpans = listOf(
                TajweedSpan(10, 15, TajweedType.IDGHAM),
                TajweedSpan(25, 29, TajweedType.QALQALAH)
            )
        )
    )

    val falaqVerses = listOf(
        Verse(
            surahNumber = 113,
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            transliteration = "Qul a‘ūdhu bi-Rabbil-falaq",
            translationEn = "Say, \"I seek refuge in the Lord of daybreak",
            translationUr = "آپ کہئے کہ میں صبح کے رب کی پناہ مانگتا ہوں۔",
            translationHi = "कह दो, \"मैं शरण लेता हूँ प्रभात के रब की,\"",
            tajweedSpans = listOf(
                TajweedSpan(21, 26, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 113,
            ayahNumber = 2,
            arabicText = "مِن شَرِّ مَا خَلَقَ",
            transliteration = "Min sharri mā khalaq",
            translationEn = "From the evil of that which He created",
            translationUr = "ہر اس چیز کی برائی سے جو اس نے پیدا کی۔",
            translationHi = "उसकी बनाई हुई हर चीज़ की बुराई से,",
            tajweedSpans = listOf(
                TajweedSpan(0, 5, TajweedType.IKHFA),
                TajweedSpan(14, 18, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 113,
            ayahNumber = 3,
            arabicText = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            transliteration = "Wa min sharri ghāsiqin idhā waqab",
            translationEn = "And from the evil of darkness when it settles",
            translationUr = "اور اندھیری رات کے شر سے جب وہ چھا جائے۔",
            translationHi = "और अँधेरी रात की बुराई से जब वह छा जाए,",
            tajweedSpans = listOf(
                TajweedSpan(3, 8, TajweedType.IKHFA),
                TajweedSpan(25, 29, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 113,
            ayahNumber = 4,
            arabicText = "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
            transliteration = "Wa min sharrin-naffāthāti fil-‘uqad",
            translationEn = "And from the evil of the blowers in knots",
            translationUr = "اور گرہوں میں پھونکنے والیوں (جادوگرنیوں) کے شر سے۔",
            translationHi = "और गांठों में फूंकने वालियों की बुराई से,",
            tajweedSpans = listOf(
                TajweedSpan(3, 8, TajweedType.IKHFA),
                TajweedSpan(13, 17, TajweedType.GHUNNAH),
                TajweedSpan(31, 36, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 113,
            ayahNumber = 5,
            arabicText = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliteration = "Wa min sharri ḥāsidin idhā ḥasad",
            translationEn = "And from the evil of an envier when he envies.\"",
            translationUr = "اور حسد کرنے والے کی برائی سے جب وہ حسد کرے۔",
            translationHi = "और ईर्ष्या करने वाले की बुराई से जब वह ईर्ष्या करे।\"",
            tajweedSpans = listOf(
                TajweedSpan(3, 8, TajweedType.IKHFA),
                TajweedSpan(26, 30, TajweedType.QALQALAH)
            )
        )
    )

    val nasVerses = listOf(
        Verse(
            surahNumber = 114,
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            transliteration = "Qul a‘ūdhu bi-Rabbin-nās",
            translationEn = "Say, \"I seek refuge in the Lord of mankind,",
            translationUr = "آپ فرما دیجیے کہ میں انسانوں کے پروردگار کی پناہ میں آتا ہوں۔",
            translationHi = "कह दो, \"मैं इंसानों के रब की शरण लेता हूँ,\"",
            tajweedSpans = listOf(
                TajweedSpan(18, 22, TajweedType.GHUNNAH),
                TajweedSpan(22, 27, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 114,
            ayahNumber = 2,
            arabicText = "مَلِكِ النَّاسِ",
            transliteration = "Malikin-nās",
            translationEn = "The Sovereign of mankind,",
            translationUr = "انسانوں کے بادشاہ کی،",
            translationHi = "इंसानों के बादशाह की,",
            tajweedSpans = listOf(
                TajweedSpan(6, 10, TajweedType.GHUNNAH),
                TajweedSpan(10, 14, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 114,
            ayahNumber = 3,
            arabicText = "إِلَٰهِ النَّاسِ",
            transliteration = "Ilāhin-nās",
            translationEn = "The God of mankind,",
            translationUr = "انسانوں کے معبود برحق کی،",
            translationHi = "इंसानों के सच्चे पूज्य की,",
            tajweedSpans = listOf(
                TajweedSpan(8, 12, TajweedType.GHUNNAH),
                TajweedSpan(12, 16, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 114,
            ayahNumber = 4,
            arabicText = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            transliteration = "Min sharril-waswāsil-khannās",
            translationEn = "From the evil of the retreating whisperer -",
            translationUr = "پیچھے ہٹ جانے والے وسوسہ ڈالنے والے کے شر سے،",
            translationHi = "पीछे हट जाने वाले बहकाने वाले शैतान की बुराई से,",
            tajweedSpans = listOf(
                TajweedSpan(0, 5, TajweedType.IKHFA),
                TajweedSpan(23, 27, TajweedType.GHUNNAH),
                TajweedSpan(27, 31, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 114,
            ayahNumber = 5,
            arabicText = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            transliteration = "Alladhī yuwaswisu fī ṣudūrin-nās",
            translationEn = "Who whispers into the breasts of mankind -",
            translationUr = "جو لوگوں کے سینوں میں وسوسے ڈالتا ہے،",
            translationHi = "जो इंसानों के सीनों में वसवसा (संशय) डालता है,",
            tajweedSpans = listOf(
                TajweedSpan(28, 32, TajweedType.GHUNNAH),
                TajweedSpan(32, 36, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 114,
            ayahNumber = 6,
            arabicText = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliteration = "Minal-jinnati wan-nās",
            translationEn = "From among the jinn and mankind.\"",
            translationUr = "خواہ وہ جنات میں سے ہو یا انسانوں میں سے۔",
            translationHi = "चाहे वह जिन्नों में से हो या इंसानों में से।\"",
            tajweedSpans = listOf(
                TajweedSpan(7, 11, TajweedType.GHUNNAH),
                TajweedSpan(18, 22, TajweedType.GHUNNAH),
                TajweedSpan(22, 26, TajweedType.MADD)
            )
        )
    )

    val kafirunVerses = listOf(
        Verse(
            surahNumber = 109,
            ayahNumber = 1,
            arabicText = "قُلْ يَا أَيُّهَا الْكَافِرُونَ",
            transliteration = "Qul yā ayyuhal-kāfirūn",
            translationEn = "Say, \"O disbelievers,",
            translationUr = "آپ فرما دیجیے کہ اے کافرو!",
            translationHi = "कह दो, \"हे काफ़िरो!",
            tajweedSpans = listOf(
                TajweedSpan(5, 11, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 109,
            ayahNumber = 2,
            arabicText = "لَا أَعْبُدُ مَا تَعْبُدُونَ",
            transliteration = "Lā a‘budu mā ta‘budūn",
            translationEn = "I do not worship what you worship.",
            translationUr = "نہ میں ان کی عبادت کرتا ہوں جنہیں تم پوجتے ہو۔",
            translationHi = "मैं उसकी इबादत नहीं करता जिसे तुम पूजते हो,",
            tajweedSpans = listOf(
                TajweedSpan(0, 4, TajweedType.MADD)
            )
        ),
        Verse(
            surahNumber = 109,
            ayahNumber = 3,
            arabicText = "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ",
            transliteration = "Wa lā antum ‘ābidūna mā a‘bud",
            translationEn = "Nor are you worshippers of what I worship.",
            translationUr = "اور نہ تم اس کی عبادت کرنے والے ہو جس کی میں عبادت کرتا ہوں۔",
            translationHi = "और न तुम उसकी इबादत करने वाले हो जिसकी मैं इबादत करता हूँ,",
            tajweedSpans = listOf(
                TajweedSpan(5, 9, TajweedType.IKHFA),
                TajweedSpan(26, 30, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 109,
            ayahNumber = 4,
            arabicText = "وَلَا أَنَا عَابِدٌ مَّا عَبَدتُّمْ",
            transliteration = "Wa lā ana ‘ābidum-mā ‘abattum",
            translationEn = "Nor will I be a worshipper of what you worship.",
            translationUr = "اور نہ میں ان کی پرستش کرنے والا ہوں جن کی تم نے پرستش کی،",
            translationHi = "और न मैं उनकी पूजा करने वाला हूँ जिनकी तुमने पूजा की,",
            tajweedSpans = listOf(
                TajweedSpan(16, 21, TajweedType.IDGHAM)
            )
        ),
        Verse(
            surahNumber = 109,
            ayahNumber = 5,
            arabicText = "وَلَا أَنتُمْ عَابِدُونَ مَا أَعْبُدُ",
            transliteration = "Wa lā antum ‘ābidūna mā a‘bud",
            translationEn = "Nor will you be worshippers of what I worship.",
            translationUr = "اور نہ تم اس کی عبادت کرنے والے ہو جس کی میں عبادت کرتا ہوں۔",
            translationHi = "और न तुम उसकी इबादत करने वाले हो जिसकी मैं इबादत करता हूँ,",
            tajweedSpans = listOf(
                TajweedSpan(5, 9, TajweedType.IKHFA),
                TajweedSpan(26, 30, TajweedType.QALQALAH)
            )
        ),
        Verse(
            surahNumber = 109,
            ayahNumber = 6,
            arabicText = "لَكُمْ دِينُكُمْ وَلِيَ دِينِ",
            transliteration = "Lakum dīnukum wa liya dīn",
            translationEn = "For you is your religion, and for me is my religion.\"",
            translationUr = "تمہارے لیے تمہارا دین اور میرے لیے میرا دین۔\"",
            translationHi = "तुम्हारे लिए तुम्हारा दीन और मेरे लिए मेरा दीन।\"",
            tajweedSpans = emptyList()
        )
    )

    val asrVerses = listOf(
        Verse(
            surahNumber = 103,
            ayahNumber = 1,
            arabicText = "وَالْعَصْرِ",
            transliteration = "Wal-‘aṣr",
            translationEn = "By time,",
            translationUr = "زمانہ کی قسم!",
            translationHi = "ज़माने की क़सम!",
            tajweedSpans = emptyList()
        ),
        Verse(
            surahNumber = 103,
            ayahNumber = 2,
            arabicText = "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ",
            transliteration = "Innal-insāna lafī khusr",
            translationEn = "Indeed, mankind is in loss,",
            translationUr = "بیشک تمام انسان سخت خسارے میں ہیں،",
            translationHi = "निस्संदेह, इंसान बड़े घाटे में है,",
            tajweedSpans = listOf(
                TajweedSpan(0, 4, TajweedType.GHUNNAH),
                TajweedSpan(8, 13, TajweedType.IKHFA)
            )
        ),
        Verse(
            surahNumber = 103,
            ayahNumber = 3,
            arabicText = "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
            transliteration = "Illalladhīna āmanū wa ‘amiluṣ-ṣāliḥāti wa tawāṣaw bil-ḥaqqi wa tawāṣaw biṣ-ṣabr",
            translationEn = "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.",
            translationUr = "سوائے ان لوگوں کے جو ایمان لائے اور نیک اعمال کیے اور ایک دوسرے کو حق کی نصیحت اور صبر کی تلقین کرتے رہے۔",
            translationHi = "सिवाय उन लोगों के जो ईमान लाए और भले काम किए और एक-दूसरे को सत्य और सब्र की ताकीद करते रहे।",
            tajweedSpans = emptyList()
        )
    )

    // Complete Surah index
    val allSurahs: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "الفاتحہ", "अल-फ़ातिहा", "The Opening", 7, RevelationType.MAKKI, fatihaVerses),
        Surah(2, "البقرة", "Al-Baqarah", "البقرہ", "अल-बक़रा", "The Cow", 286, RevelationType.MADANI),
        Surah(3, "آل عمران", "Ali 'Imran", "آل عمران", "आल इमरान", "Family of Imran", 200, RevelationType.MADANI),
        Surah(4, "النساء", "An-Nisa", "النساء", "अन-निसा", "The Women", 176, RevelationType.MADANI),
        Surah(5, "المائدة", "Al-Ma'idah", "المائدہ", "अल-माइदा", "The Table Spread", 120, RevelationType.MADANI),
        Surah(6, "الأنعام", "Al-An'am", "الانعام", "अल-अनआम", "The Cattle", 165, RevelationType.MAKKI),
        Surah(7, "الأعراف", "Al-A'raf", "الاعراف", "अल-अअराफ़", "The Heights", 206, RevelationType.MAKKI),
        Surah(8, "الأنفال", "Al-Anfal", "الانفال", "अल-अनफ़ाल", "The Spoils of War", 75, RevelationType.MADANI),
        Surah(9, "التوبة", "At-Tawbah", "التوبہ", "अत-तौबा", "The Repentance", 129, RevelationType.MADANI),
        Surah(10, "يونس", "Yunus", "یونس", "यूनुस", "Jonah", 109, RevelationType.MAKKI),
        Surah(18, "الكهف", "Al-Kahf", "الکہف", "अल-कहफ़", "The Cave", 110, RevelationType.MAKKI),
        Surah(36, "يس", "Yasin", "یس", "यासीन", "Ya-Sin (Heart of Quran)", 83, RevelationType.MAKKI),
        Surah(55, "الرحمن", "Ar-Rahman", "الرحمن", "अर-रहमान", "The Beneficent", 78, RevelationType.MADANI),
        Surah(56, "الواقعة", "Al-Waqi'ah", "الواقعہ", "अल-वाक़िया", "The Inevitable", 96, RevelationType.MAKKI),
        Surah(67, "الملك", "Al-Mulk", "الملک", "अल-मुल्क", "The Sovereignty", 30, RevelationType.MAKKI),
        Surah(103, "العصر", "Al-Asr", "العصر", "अल-अस्र", "The Declining Day", 3, RevelationType.MAKKI, asrVerses),
        Surah(108, "الكوثر", "Al-Kawthar", "الکوثر", "अल-कौसर", "The Abundance", 3, RevelationType.MAKKI),
        Surah(109, "الكافرون", "Al-Kafirun", "الکافرون", "अल-काफ़िरून", "The Disbelievers", 6, RevelationType.MAKKI, kafirunVerses),
        Surah(110, "النصر", "An-Nasr", "النصر", "अन-नस्र", "The Divine Support", 3, RevelationType.MADANI),
        Surah(111, "المسد", "Al-Masad", "المسد", "अल-मसद", "The Palm Fiber", 5, RevelationType.MAKKI),
        Surah(112, "الإخلاص", "Al-Ikhlas", "الاخلاص", "अल-इख़्लास", "The Sincerity (Tawheed)", 4, RevelationType.MAKKI, ikhlasVerses),
        Surah(113, "الفلق", "Al-Falaq", "الفلق", "अल-फ़लक़", "The Daybreak", 5, RevelationType.MAKKI, falaqVerses),
        Surah(114, "الناس", "An-Nas", "الناس", "अन-नास", "Mankind", 6, RevelationType.MAKKI, nasVerses)
    )

    // Module 2: Dedicated Readers & Authentic Supplications
    val supplications: List<Supplication> = listOf(
        Supplication(
            id = "ayat_kursi",
            titleEn = "Ayat al-Kursi (The Throne Verse)",
            titleUr = "آیۃ الکرسی (عظیم ترین آیت)",
            titleHi = "आयतुल कुर्सी (महानतम आयत)",
            titleAr = "آية الكرسي",
            category = DuaCategory.AYAT_UL_KURSI,
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliteration = "Allāhu lā ilāha illā Huwal-Ḥayyul-Qayyūm. Lā ta'khudhuhū sinatuw-wa lā nawm. Lahū mā fis-samāwāti wa mā fil-arḍ. Man dhalladhī yashfa‘u ‘indahū illā bi-idhnih. Ya‘lamu mā bayna aydīhim wa mā khalfahum, wa lā yuḥīṭūna bi-shay'im-min ‘ilmihī illā bimā shā'. Wasi‘a kursiyyuhus-samāwāti wal-arḍ, wa lā ya'ūduhū ḥifẓuhumā, wa Huwal-‘Aliyyul-‘Aẓīm.",
            translationEn = "Allah! There is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            translationUr = "اللہ وہ ہے جس کے سوا کوئی معبود نہیں، وہ ہمیشہ زندہ رہنے والا اور سب کو سنبھالنے والا ہے۔ نہ اسے اونگھ آتی ہے نہ نیند۔ اسی کا ہے جو کچھ آسمانوں میں ہے اور جو کچھ زمین میں ہے۔ کون ہے جو اس کی اجازت کے بغیر اس کے حضور سفارش کر سکے؟ وہ جانتا ہے جو کچھ بندوں کے سامنے ہے اور جو کچھ ان کے پیچھے ہے۔ اور وہ اس کے علم میں سے کسی چیز کا احاطہ نہیں کر سکتے مگر جتنا وہ چاہے۔ اس کی کرسی آسمانوں اور زمین پر محیط ہے، اور ان دونوں کی حفاظت اس پر گراں نہیں گزرتی، اور وہ سب سے برتر اور عظمت والا ہے۔",
            translationHi = "अल्लाह! उसके सिवा कोई पूज्य नहीं, वह सदा जीवित रहने वाला और सब का थामने वाला है। न उसे ऊंघ आती है और न नींद। उसी का है जो कुछ आकाशों में है और जो कुछ धरती में है। कौन है जो उसके पास उसकी अनुमति के बिना सिफ़ारिश कर सके? वह जानता है जो कुछ उनके सामने है और जो कुछ उनके पीछे है। और वे उसके ज्ञान में से किसी चीज़ पर हावी नहीं हो सकते सिवाय उसके जो वह चाहे। उसकी कुर्सी सारे आकाशों और धरती को घेरे हुए है, और उन दोनों की हिफ़ाज़त उसे थकाती नहीं, और वह सर्वोच्च, अत्यंत महान है।",
            virtue = "The Prophet (ﷺ) said: 'Whoever recites Ayat al-Kursi after every obligatory prayer, nothing prevents him from entering Paradise except death.' (Sunan An-Nasa'i, Sahih by Ibn Hibban).",
            reference = "Surah Al-Baqarah (2:255) | Sahih Bukhari 5010",
            targetCount = 1,
            tajweedSpans = listOf(
                TajweedSpan(0, 5, TajweedType.MADD),
                TajweedSpan(100, 106, TajweedType.IKHFA),
                TajweedSpan(210, 218, TajweedType.IDGHAM)
            )
        ),
        Supplication(
            id = "amanar_rasul",
            titleEn = "Amanar-Rasul (Last 2 Verses of Al-Baqarah)",
            titleUr = "آمن الرسول (سورۃ البقرہ کی آخری دو آیات)",
            titleHi = "आमनर-रसूल (सूरह बक़रा की अंतिम दो आयतें)",
            titleAr = "آمن الرسول (خواتيم سورة البقرة)",
            category = DuaCategory.AMANAR_RASUL,
            arabicText = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ ۝ لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
            transliteration = "Āmanar-Rasūlu bimā unzila ilayhi mir-Rabbihī wal-mu'minūn...",
            translationEn = "The Messenger has believed in what was revealed to him from his Lord, and [so have] the believers... Our Lord, do not impose blame upon us if we forget or make a mistake... Pardon us; forgive us; and have mercy on us. You are our protector, so give us victory over the disbelieving people.",
            translationUr = "رسول اس پر ایمان لائے جو ان کے رب کی طرف سے ان پر نازل ہوا اور تمام مومنین بھی... اے ہمارے رب! اگر ہم بھول جائیں یا خطا کر بیٹھیں تو ہماری گرفت نہ فرما... اور ہمیں معاف فرما دے اور ہماری مغفرت فرما اور ہم پر رحم فرما، تو ہی ہمارا کارساز ہے، پس کافر قوم پر ہماری مدد فرما۔",
            translationHi = "रसूल उस पर ईमान लाया जो उसके रब की ओर से उस पर अवतरित हुआ और मोमिन भी... हे हमारे रब! यदि हम भूल जाएं या चूक जाएं तो हमें न पकड़ना... और हमें क्षमा कर दे, हम पर दया कर, तू ही हमारा संरक्षक है, अतः काफ़िरों पर हमारी सहायता कर।",
            virtue = "The Prophet (ﷺ) said: 'Whoever recites the last two verses of Surah Al-Baqarah at night, they will suffice him (from all evil).' (Sahih al-Bukhari 5009, Sahih Muslim 807).",
            reference = "Surah Al-Baqarah (2:285-286)",
            targetCount = 1
        ),
        Supplication(
            id = "dua_yunus",
            titleEn = "Dua Yunus (Ayah Kareemah)",
            titleUr = "دعا یونس (آیت کریمہ)",
            titleHi = "दुआ यूनुस (आयत करीमा)",
            titleAr = "دعاء يونس عليه السلام",
            category = DuaCategory.ESSENTIAL_DUAS,
            arabicText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            transliteration = "Lā ilāha illā Anta subḥānaka innī kuntu minaẓ-ẓālimīn",
            translationEn = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            translationUr = "تیرے سوا کوئی معبود نہیں، تو پاک ہے، بیشک میں ہی قصورواروں (ظالموں) میں سے ہوں۔",
            translationHi = "तेरे सिवा कोई सच्चा पूज्य नहीं, तू अत्यंत पवित्र है, निस्संदेह मैं ही ज़ालिमों (दोषियों) में से था।",
            virtue = "The Prophet (ﷺ) said: 'No Muslim suppliant ever prays with these words in any hardship but that Allah answers his prayer.' (Jami` at-Tirmidhi 3505, Sahih).",
            reference = "Surah Al-Anbiya (21:87) | Jami` at-Tirmidhi 3505",
            targetCount = 40
        ),
        Supplication(
            id = "sayyidul_istighfar",
            titleEn = "Sayyidul Istighfar (Chief of Forgiveness)",
            titleUr = "سید الاستغفار (توبہ کا سردار)",
            titleHi = "सैयद-उल-इस्तिग़फ़ार (क्षमा की सर्वोच्च दुआ)",
            titleAr = "سَيِّدُ الاسْتِغْفَارِ",
            category = DuaCategory.ESSENTIAL_DUAS,
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma Anta Rabbī lā ilāha illā Anta, khalaqtanī wa anā ‘abduka, wa anā ‘alā ‘ahdika wa wa‘dika mastaṭa‘tu, a‘ūdhu bika min sharri mā ṣana‘tu, abū'u laka bini‘matika ‘alayya, wa abū'u laka bidhanbī faghfir lī fa-innahū lā yaghfirudh-dhunūba illā Anta.",
            translationEn = "O Allah, You are my Lord, none has the right to be worshipped but You. You created me and I am Your slave, and I am faithful to my covenant and promise to You as much as I can. I seek refuge in You from the evil of what I have done. I acknowledge before You all the blessings You have bestowed upon me, and I confess to You my sin. So forgive me, for none can forgive sins except You.",
            translationUr = "اے اللہ! تو ہی میرا رب ہے، تیرے سوا کوئی معبود نہیں، تو نے مجھے پیدا کیا اور میں تیرا بندہ ہوں اور میں اپنی استطاعت کے مطابق تیرے عہد اور وعدے پر قائم ہوں۔ میں اپنے اعمال کے شر سے تیری پناہ مانگتا ہوں۔ میں اپنے اوپر تیری تمام نعمتوں کا اعتراف کرتا ہوں اور اپنے گناہوں کا اقرار کرتا ہوں، پس تو مجھے بخش دے کیونکہ تیرے سوا کوئی گناہوں کو معاف نہیں کر سکتا۔",
            translationHi = "हे अल्लाह! तू ही मेरा पालनहार है, तेरे सिवा कोई पूज्य नहीं। तूने मुझे पैदा किया और मैं तेरा बन्दा हूँ और अपनी सामर्थ्य के अनुसार तेरे वादे पर क़ायम हूँ। मैं अपने कर्मों की बुराई से तेरी शरण चाहता हूँ। मैं मुझ पर तेरी नेमतों को स्वीकार करता हूँ और अपने पापों को स्वीकार करता हूँ, अतः मुझे क्षमा कर दे, क्योंकि तेरे सिवा कोई पापों को क्षमा नहीं कर सकता।",
            virtue = "The Prophet (ﷺ) said: 'Whoever says it during the day with firm faith and dies that day before evening, will be from the people of Paradise; and whoever says it at night and dies before morning, will be from the people of Paradise.' (Sahih al-Bukhari 6306).",
            reference = "Sahih al-Bukhari 6306",
            targetCount = 1
        ),
        Supplication(
            id = "dua_qunoot",
            titleEn = "Dua-e-Qunoot (Witr Prayer Supplication)",
            titleUr = "دعائے قنوت (نماز وتر)",
            titleHi = "दुआ-ए-क़ुनूत (वित्र नमाज़ की दुआ)",
            titleAr = "دُعَاءُ الْقُنُوتِ فِي الْوِتْرِ",
            category = DuaCategory.ESSENTIAL_DUAS,
            arabicText = "اللَّهُمَّ اهْدِنِي فِيمَنْ هَدَيْتَ، وَعَافِنِي فِيمَنْ عَافَيْتَ، وَتَوَلَّنِي فِيمَنْ تَوَلَّيْتَ، وَبَارِكْ لِي فِيمَا أَعْطَيْتَ، وَقِنِي شَرَّ مَا قَضَيْتَ، فَإِنَّكَ تَقْضِي وَلَا يُقْضَى عَلَيْكَ، وَإِنَّهُ لَا يَذِلُّ مَنْ وَالَيْتَ، وَلَا يَعِزُّ مَنْ عَادَيْتَ، تَبَارَكْتَ رَبَّنَا وَتَعَالَيْتَ",
            transliteration = "Allāhummah-dinī fīman hadayt, wa ‘āfinī fīman ‘āfayt, wa tawallanī fīman tawallayt, wa bārik lī fīmā a‘ṭayt, wa qinī sharra mā qaḍayt, fa-innaka taqḍī wa lā yuqḍā ‘alayk, wa innahū lā yadhillu maw-wālayt, wa lā ya‘izzu man ‘ādayt, tabārakta Rabbanā wa ta‘ālayt.",
            translationEn = "O Allah, guide me among those You have guided, grant me health among those You have granted health, look after me among those You have looked after, bless for me what You have given, and save me from the evil of what You have decreed. For indeed You decree and none decrees over You. Indeed, he is not disgraced whom You befriend, and he is not honored whom You take as an enemy. Blessed are You, our Lord, and Exalted.",
            translationUr = "اے اللہ! مجھے ہدایت دے ان لوگوں میں جنہیں تو نے ہدایت دی، اور مجھے عافیت دے ان میں جنہیں تو نے عافیت دی، اور میرا کارساز بن ان میں جن کا تو کارساز بنا، اور جو کچھ تو نے عطا فرمایا اس میں مجھے برکت دے، اور اپنے فیصلے کے شر سے مجھے بچا، کیونکہ تو ہی فیصلہ فرماتا ہے اور تیرے خلاف کوئی فیصلہ نہیں ہو سکتا۔ بیشک جسے تو دوست رکھے وہ ذلیل نہیں ہو سکتا اور جس سے تو دشمنی رکھے وہ عزت نہیں پا سکتا۔ اے ہمارے پروردگار! تو بڑی برکت والا اور بلند و بالا ہے۔",
            translationHi = "हे अल्लाह! मुझे हिदायत दे उन लोगों के साथ जिन्हें तूने हिदायत दी, और मुझे सलामती दे उनके साथ जिन्हें तूने सलामती दी, और मेरी देख-रेख कर उनके साथ जिनकी तूने देख-रेख की, और जो कुछ तूने दिया उसमें बरकत दे, और अपने तय किए हुए फ़ैसले की बुराई से मुझे बचा। क्योंकि तू ही फ़ैसला करता है और तेरे ख़िलाफ़ कोई फ़ैसला नहीं हो सकता। जिसे तू अपना दोस्त बनाए वह ज़लील नहीं हो सकता, और जिससे तू दुश्मनी रखे वह इज़्ज़त नहीं पा सकता। हे हमारे रब! तू अत्यंत बरकत वाला और सर्वोच्च है।",
            virtue = "Taught by the Prophet (ﷺ) to Al-Hasan ibn Ali (RA) for the Witr prayer.",
            reference = "Sunan Abi Dawud 1425 | Jami` at-Tirmidhi 464 (Sahih)",
            targetCount = 1
        ),
        // Authentic Wazifas with tap counts
        Supplication(
            id = "durood_ibrahim",
            titleEn = "Durood Ibrahim (Blessings upon the Prophet)",
            titleUr = "درودِ ابراہیمی (صلاۃ و سلام)",
            titleHi = "दुरूद-ए-इब्राहीमी (सल्लल्लाहु अलैहि वसल्लम)",
            titleAr = "الصَّلَاةُ الإِبْرَاهِيمِيَّةُ",
            category = DuaCategory.WAZIFAS,
            arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliteration = "Allāhumma ṣalli ‘alā Muḥammadiw-wa ‘alā āli Muḥammad, kamā ṣallayta ‘alā Ibrāhīma wa ‘alā āli Ibrāhīm, innaka Ḥamīdum-Majīd...",
            translationEn = "O Allah, send prayers upon Muhammad and upon the family of Muhammad, as You sent prayers upon Ibrahim and upon the family of Ibrahim; indeed, You are Praiseworthy and Glorious...",
            translationUr = "اے اللہ! درود بھیج محمد (ﷺ) پر اور ان کی آل پر جیسا کہ تو نے درود بھیجا ابراہیم (علیہ السلام) پر اور ان کی آل پر، بیشک تو تعریف کے لائق اور بزرگی والا ہے...",
            translationHi = "हे अल्लाह! रहमत नाज़िल फ़रमा मुहम्मद (ﷺ) पर और उनके परिवार पर, जैसे तूने रहमत नाज़िल फ़रमाई इब्राहीम (अलैहि.) पर और उनके परिवार पर, निस्संदेह तू प्रशंसनीय और महान है...",
            virtue = "The Prophet (ﷺ) said: 'Whoever sends blessings upon me once, Allah will send blessings upon him tenfold, erase ten sins, and raise him ten degrees.' (Sunan an-Nasa'i 1297, Sahih).",
            reference = "Sahih al-Bukhari 3370",
            targetCount = 10
        ),
        Supplication(
            id = "astaghfirullah_100",
            titleEn = "Astaghfirullah (Seeking Forgiveness 100x)",
            titleUr = "استغفار (روزانہ ۱۰۰ مرتبہ)",
            titleHi = "अस्तग़फ़िरुल्लाह (प्रतिदिन 100 बार)",
            titleAr = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            category = DuaCategory.WAZIFAS,
            arabicText = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullāha wa atūbu ilayh",
            translationEn = "I seek the forgiveness of Allah and turn to Him in repentance.",
            translationUr = "میں اللہ سے بخشش مانگتا ہوں اور اسی کے حضور توبہ کرتا ہوں۔",
            translationHi = "मैं अल्लाह से क्षमा माँगता हूँ और उसी की ओर तौबा करता हूँ।",
            virtue = "The Messenger of Allah (ﷺ) said: 'By Allah, I seek Allah's forgiveness and repent to Him more than seventy (and in Muslim: one hundred) times a day.'",
            reference = "Sahih al-Bukhari 6307 | Sahih Muslim 2702",
            targetCount = 100
        ),
        Supplication(
            id = "la_hawla_33",
            titleEn = "La Hawla (Treasure of Paradise)",
            titleUr = "لا حول ولا قوة إلا بالله (جنت کا خزانہ)",
            titleHi = "ला हवला वला क़ुव्वता (जन्नत का ख़ज़ाना)",
            titleAr = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            category = DuaCategory.WAZIFAS,
            arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Lā ḥawla wa lā quwwata illā billāhil-‘Aliyyil-‘Aẓīm",
            translationEn = "There is no might nor power except with Allah, the Most High, the Most Great.",
            translationUr = "گناہوں سے بچنے کی طاقت اور نیکی کرنے کی قوت نہیں مگر اللہ کی توفیق سے جو بلند و برتر اور عظیم ہے۔",
            translationHi = "बुराई से बचने और भलाई करने की कोई ताक़त नहीं सिवाय अल्लाह की मदद के, जो सर्वोच्च और महान है।",
            virtue = "The Prophet (ﷺ) said to Abu Musa (RA): 'Shall I not tell you of a treasure from the treasures of Paradise? Say: La hawla wa la quwwata illa billah.'",
            reference = "Sahih al-Bukhari 4205 | Sahih Muslim 2704",
            targetCount = 33
        ),
        Supplication(
            id = "hasbunallahu_7",
            titleEn = "Hasbunallahu (Sufficient is Allah)",
            titleUr = "حسبنا الله ونعم الوكيل (اللہ کافی ہے)",
            titleHi = "हसबुनल्लाहु वने'मल वकील (अल्लाह पर्याप्त है)",
            titleAr = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            category = DuaCategory.WAZIFAS,
            arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ ۝ نِعْمَ الْمَوْلَىٰ وَنِعْمَ النَّصِيرُ",
            transliteration = "Ḥasbunallāhu wa ni‘mal-Wakīl, ni‘mal-Mawlā wa ni‘man-Naṣīr",
            translationEn = "Allah is sufficient for us, and He is the best Disposer of affairs. An excellent Protector and an excellent Helper.",
            translationUr = "ہمیں اللہ ہی کافی ہے اور وہ بہترین کارساز ہے۔ کیا ہی اچھا مددگار اور کیا ہی بہترین حامی!",
            translationHi = "हमारे लिए अल्लाह ही काफ़ी है और वह बेहतरीन कार्य-साधक है। क्या ही उत्तम संरक्षक और क्या ही उत्तम मददगार!",
            virtue = "Uttered by Prophet Ibrahim (AS) when thrown into the fire, and by Prophet Muhammad (ﷺ) and the Sahabah at Uhud.",
            reference = "Surah Ali 'Imran (3:173) | Sahih al-Bukhari 4563",
            targetCount = 7
        ),
        Supplication(
            id = "subhanallahi_bihamdihi",
            titleEn = "Subhanallahi wa Bihamdihi (100x Sins Forgiven)",
            titleUr = "سبحان اللہ وبحمدہ سبحان اللہ العظیم",
            titleHi = "सुब्हानल्लाहि वबिहम्दिही (100 बार गुनाह माफ़)",
            titleAr = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ",
            category = DuaCategory.WAZIFAS,
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "Subḥānallāhi wa biḥamdih, Subḥānallāhil-‘Aẓīm",
            translationEn = "Glory be to Allah and His is the praise; Glory be to Allah the Most Supreme.",
            translationUr = "پاک ہے اللہ اپنی حمد کے ساتھ، پاک ہے اللہ جو بڑی عظمت والا ہے۔",
            translationHi = "अल्लाह पवित्र है अपनी प्रशंसा के साथ, अल्लाह पवित्र है जो अत्यंत महान है।",
            virtue = "The Prophet (ﷺ) said: 'Two words are light on the tongue, heavy on the Balance, beloved to the Most Merciful: Subhanallahi wa bihamdihi, Subhanallahil-Azeem.'",
            reference = "Sahih al-Bukhari 6406 | Sahih Muslim 2694",
            targetCount = 100
        )
    )

    // Module 4: Authentic Hadiths from Sahih Sitta
    val authenticHadiths: List<Hadith> = listOf(
        Hadith(
            id = "h1",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 1",
            chapter = "Book of Revelation (كتاب بدء الوحي)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Muttafaq 'Alayh)",
            sanad = "Narrated 'Umar bin Al-Khattab (رضي الله عنه) from the Messenger of Allah (ﷺ)",
            matnArabic = "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا، فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ",
            matnEn = "Actions are judged by intentions, and every person will have only what they intended. Whoever emigrated for worldly benefit or for a woman to marry, their emigration is for what they emigrated for.",
            matnUr = "اعمال کا دارومدار نیتوں پر ہے اور ہر شخص کے لیے وہی ہے جس کی اس نے نیت کی۔ پس جس کی ہجرت دنیا کے حصول یا کسی عورت سے نکاح کے لیے ہو تو اس کی ہجرت اسی چیز کے لیے ہے جس کی طرف اس نے ہجرت کی۔",
            matnHi = "कर्मों का दारोमदार नीयतों पर है, और प्रत्येक व्यक्ति के लिए वही है जिसकी उसने नीयत की। अतः जिसकी हिजरत दुनिया पाने या किसी स्त्री से विवाह के लिए हो, तो उसकी हिजरत उसी के लिए होगी।"
        ),
        Hadith(
            id = "h2",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 13",
            chapter = "Book of Belief (كتاب الإيمان)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) from the Prophet (ﷺ)",
            matnArabic = "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
            matnEn = "None of you truly believes until he loves for his brother (in humanity & faith) what he loves for himself.",
            matnUr = "تم میں سے کوئی شخص اس وقت تک سچا مومن نہیں ہو سکتا جب تک کہ وہ اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
            matnHi = "तुम में से कोई व्यक्ति उस समय तक सच्चा मोमिन नहीं हो सकता जब तक कि वह अपने भाई के लिए वही पसंद न करे जो अपने लिए पसंद करता है।"
        ),
        Hadith(
            id = "h3",
            book = "Sahih Muslim",
            hadithNumber = "Hadith 223",
            chapter = "Book of Purification (كتاب الطهارة)",
            topic = HadithTopic.PRAYER,
            grading = "Sahih",
            sanad = "Narrated Abu Malik Al-Ash'ari (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ، وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ أَوْ تَمْلأُ مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ، وَالصَّلاَةُ نُورٌ",
            matnEn = "Cleanliness and purification is half of faith, and 'Alhamdulillah' (Praise be to Allah) fills the scale, and 'SubhanAllah' and 'Alhamdulillah' fill whatever is between heavens and earth, and prayer (Salah) is light.",
            matnUr = "طہارت و پاکیزگی نصف ایمان ہے، اور 'الحمد لله' نیکیوں کے پلڑے کو بھر دیتا ہے، اور 'سبحان الله' اور 'الحمد لله' آسمانوں اور زمین کے درمیانی فاصلے کو بھر دیتے ہیں، اور نماز سراپا نور ہے۔",
            matnHi = "पवित्रता और सफ़ाई आधा ईमान है, और 'अल्हम्दुलिल्लाह' तराज़ू को भर देता है, और 'सुब्हानल्लाह' व 'अल्हम्दुलिल्لاह' आकाश और धरती के बीच की जगह को भर देते हैं, और नमाज़ प्रकाश है।"
        ),
        Hadith(
            id = "h4",
            book = "Jami` at-Tirmidhi",
            hadithNumber = "Hadith 2003",
            chapter = "Book of Righteousness and Relations (كتاب البر والصلة)",
            topic = HadithTopic.AKHLAQ,
            grading = "Sahih (Hasan Sahih)",
            sanad = "Narrated Abu Darda (رضي الله عنه) that the Prophet (ﷺ) said",
            matnArabic = "مَا شَيْءٌ أَثْقَلُ فِي مِيزَانِ الْمُؤْمِنِ يَوْمَ الْقِيَامَةِ مِنْ خُلُقٍ حَسَنٍ، وَإِنَّ اللَّهَ لَيُبْغِضُ الْفَاحِشَ الْبَذِيءَ",
            matnEn = "Nothing is heavier on the scale of a believer on the Day of Resurrection than good character and noble manners. Indeed, Allah dislikes the foul-mouthed and obscene person.",
            matnUr = "قیامت کے دن مومن کے میزان میں اچھے اخلاق سے زیادہ بھاری کوئی چیز نہیں ہوگی، اور بیشک اللہ تعالی بے ہودہ گو اور بدزبان شخص سے نفرت فرماتا ہے۔",
            matnHi = "क़ियामत के दिन मोमिन के तराज़ू में उत्तम चरित्र (अच्छे अख़लाक़) से भारी कोई चीज़ नहीं होगी, और निस्संदेह अल्लाह अश्लील व अभद्र बात करने वाले से नफ़रत करता है।"
        ),
        Hadith(
            id = "h5",
            book = "Sahih al-Bukhari",
            hadithNumber = "Hadith 1901",
            chapter = "Book of Fasting (كتاب الصوم)",
            topic = HadithTopic.FASTING,
            grading = "Sahih (Muttafaq 'Alayh)",
            sanad = "Narrated Abu Huraira (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
            matnEn = "Whoever fasts during the month of Ramadan out of sincere faith and seeking Allah's reward, all his previous sins will be forgiven.",
            matnUr = "جس شخص نے رمضان کے روزے ایمان اور ثواب کی نیت سے رکھے، اس کے پچھلے تمام گناہ معاف کر دیے جاتے ہیں۔",
            matnHi = "जिस व्यक्ति ने रमज़ान के रोज़े ईमान और सवाब की नीयत से रखे, उसके पिछले सारे गुनाह माफ़ कर दिए जाते हैं।"
        ),
        Hadith(
            id = "h6",
            book = "Sunan Abi Dawud",
            hadithNumber = "Hadith 1694",
            chapter = "Book of Zakat (كتاب الزكاة)",
            topic = HadithTopic.SADAQAH,
            grading = "Sahih",
            sanad = "Narrated Abu Huraira (رضي الله عنه) from the Prophet (ﷺ)",
            matnArabic = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا، وَمَا تَوَاضَعَ أَحَدٌ لِلَّهِ إِلَّا رَفَعَهُ اللَّهُ",
            matnEn = "Charity does not decrease wealth, and Allah increases the honor of a person who pardons others, and no one humbles themselves for the sake of Allah except that Allah elevates him.",
            matnUr = "صدقہ کسی مال میں کمی نہیں کرتا، اور معاف کرنے والے کی اللہ صرف عزت ہی بڑھاتا ہے، اور جو کوئی اللہ کی خاطر عاجزی اختیار کرتا ہے اللہ اسے بلندیاں عطا فرماتا ہے۔",
            matnHi = "दान (सदक़ा) माल में कोई कमी नहीं करता, और क्षमा करने वाले का अल्लाह सम्मान ही बढ़ाता है, और जो कोई अल्लाह की ख़ातिर विनम्रता अपनाता है, अल्लाह उसका दर्जा बुलंद फ़रमाता है।"
        ),
        Hadith(
            id = "h7",
            book = "Sunan an-Nasa'i",
            hadithNumber = "Hadith 3104",
            chapter = "Book of Jihad & Good Conduct (كتاب الجهاد)",
            topic = HadithTopic.FAMILY,
            grading = "Hasan Sahih",
            sanad = "Narrated Mu'awiyah ibn Jahima that his father came to the Prophet (ﷺ) and said: 'O Messenger of Allah, I wish to strive in jihad and come to consult you.' The Prophet asked: 'Do you have a mother?' He replied: 'Yes.' The Prophet said:",
            matnArabic = "فَالْزَمْهَا، فَإِنَّ الْجَنَّةَ تَحْتَ رِجْلَيْهَا",
            matnEn = "Then stay with her and serve her, for indeed Paradise lies beneath her feet.",
            matnUr = "پس اس کی خدمت میں لگے رہو، کیونکہ بیشک جنت اسی کے قدموں تلے ہے۔",
            matnHi = "तो उसकी सेवा में लगा रह, क्योंकि निस्संदेह जन्नत उसी के क़दमों के नीचे है।"
        ),
        Hadith(
            id = "h8",
            book = "Sunan Ibn Majah",
            hadithNumber = "Hadith 224",
            chapter = "The Book of the Sunnah (المقدمة)",
            topic = HadithTopic.FAITH,
            grading = "Sahih (Albani)",
            sanad = "Narrated Anas bin Malik (رضي الله عنه) that the Messenger of Allah (ﷺ) said",
            matnArabic = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
            matnEn = "Seeking beneficial knowledge is an absolute religious obligation upon every Muslim (male and female).",
            matnUr = "دین کا علم حاصل کرنا ہر مسلمان (مرد اور عورت) پر فرض ہے۔",
            matnHi = "ज्ञान प्राप्त करना प्रत्येक मुस्लिम (पुरुष और स्त्री) पर फ़र्ज़ (अनिवार्य) है।"
        )
    )

    // Module 5: 99 Names of Allah (Selected rich set for learning and flashcards)
    val asmaUlHusna: List<AsmaName> = listOf(
        AsmaName(1, "الرَّحْمَٰنُ", "Ar-Rahman", "The Entirely Merciful", "نہایت مہربان", "अत्यंत दयावान", "His boundless mercy encompasses every single created thing in existence.", "Surah Al-Fatihah (1:3)"),
        AsmaName(2, "الرَّحِيمُ", "Ar-Rahim", "The Especially Merciful", "نہایت رحم فرمانے والا", "विशेष कृपावान", "His specific, tender grace showered upon those who believe.", "Surah Al-Baqarah (2:143)"),
        AsmaName(3, "الْمَلِكُ", "Al-Malik", "The King / Sovereign", "حقیقی بادشاہ", "सर्वसत्ताधीश सम्राट", "The absolute ruler and sovereign possessor of the entire cosmos.", "Surah Al-Hashr (59:23)"),
        AsmaName(4, "الْقُدُّوسُ", "Al-Quddus", "The Most Sacred / Pure", "نہایت پاک ذات", "परम पवित्र", "Free and unblemished from any imperfection, error, or defect.", "Surah Al-Jumu'ah (62:1)"),
        AsmaName(5, "السَّلَامُ", "As-Salam", "The Source of Peace", "سلامتی دینے والا", "शान्ति का स्रोत", "The giver of serenity to the hearts and safety from all harms.", "Surah Al-Hashr (59:23)"),
        AsmaName(6, "الْمُؤْمِنُ", "Al-Mu'min", "The Bestower of Security", "امن و امان دینے والا", "सुरक्षा प्रदान करने वाला", "The one who affirms truth and grants safety from His punishment.", "Surah Al-Hashr (59:23)"),
        AsmaName(7, "الْمُهَيْمِنُ", "Al-Muhaymin", "The Guardian / Overseer", "نگہبان و محافظ", "सबका संरक्षक", "The watchful guardian who preserves and oversees all affairs.", "Surah Al-Hashr (59:23)"),
        AsmaName(8, "الْعَزِيزُ", "Al-'Aziz", "The Almighty / Invincible", "سب پر غالب و زبردست", "सर्वशक्तिमान", "The all-mighty whom none can overcome or defeat.", "Surah Al-Baqarah (2:129)"),
        AsmaName(9, "الْجَبَّارُ", "Al-Jabbar", "The Restorer / Compeller", "شکستہ دلوں کو جوڑنے والا", "टूटे दिलों को संभालने वाला", "The one who mends what is broken in His servants' hearts and executes His will.", "Surah Al-Hashr (59:23)"),
        AsmaName(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "The Supreme / Majestic", "بڑائی والا", "सर्वोच्च महिमावान", "The one to whom alone true majesty and greatness belong.", "Surah Al-Hashr (59:23)"),
        AsmaName(11, "الْخَالِقُ", "Al-Khaliq", "The Creator", "پیدا فرمانے والا", "सृष्टिकर्ता", "The creator who brings things into being out of nothingness.", "Surah Al-An'am (6:102)"),
        AsmaName(12, "الْبَارِئُ", "Al-Bari'", "The Originator / Maker", "عدم سے وجود بخشنے والا", "आरंभकर्ता", "The inventor who forms creatures with flawless balance.", "Surah Al-Hashr (59:24)"),
        AsmaName(13, "الْمُصَوِّرُ", "Al-Musawwir", "The Fashioner of Forms", "صورتیں بنانے والا", "रूप प्रदान करने वाला", "The artisan who crafts every creature with unique distinction.", "Surah Al-Hashr (59:24)"),
        AsmaName(14, "الْغَفَّارُ", "Al-Ghaffar", "The Perpetual Forgiver", "بار بار بخشنے والا", "सदा क्षमाशील", "The one who conceals flaws and repeatedly forgives repentance.", "Surah Nuh (71:10)"),
        AsmaName(15, "الْقَهَّارُ", "Al-Qahhar", "The Subduer", "سب پر مکمل قابض", "सब पर प्रभुत्वशाली", "The omnipotent conqueror to whose majesty everything bows.", "Surah Ar-Ra'd (13:16)"),
        AsmaName(16, "الْوَهَّابُ", "Al-Wahhab", "The Bestower of Gifts", "بے غرض عطا فرمانے والا", "उदार दाता", "The boundless giver who bestows blessings without requesting return.", "Surah Ali 'Imran (3:8)"),
        AsmaName(17, "الرَّزَّاقُ", "Ar-Razzaq", "The All-Provider", "سب کو رزق دینے والا", "सबका अन्नदाता", "The sustainer who guarantees sustenance to all life on earth.", "Surah Adh-Dhariyat (51:58)"),
        AsmaName(18, "الْفَتَّاحُ", "Al-Fattah", "The Opener / Judge", "راہیں کھولنے والا", "मार्ग खोलने वाला", "The opener of the doors of mercy, relief, and righteous victory.", "Surah Saba (34:26)"),
        AsmaName(19, "الْعَلِيمُ", "Al-'Alim", "The All-Knowing", "سب کچھ جاننے والا", "सर्वज्ञ", "Whose knowledge encompasses the unseen, the past, present, and future.", "Surah Al-Baqarah (2:29)"),
        AsmaName(20, "الْقَابِضُ", "Al-Qabid", "The Restrainer", "تنگی و وسعت کا مالک", "नियंत्रक", "The one who withholds and tightens provision according to divine wisdom.", "Surah Al-Baqarah (2:245)"),
        AsmaName(21, "الْبَاسِطُ", "Al-Basit", "The Expander / Reliever", "کشادگی بخشنے والا", "विस्तारकर्ता", "The one who opens up bounty, wealth, and joy upon hearts.", "Surah Al-Baqarah (2:245)"),
        AsmaName(22, "الْخَافِضُ", "Al-Khafid", "The Humbler of the Arrogant", "پست کرنے والا", "अहंकारियों को झुकाने वाला", "The one who lowers the haughty and corrupt.", "Sunan at-Tirmidhi 3507"),
        AsmaName(23, "الرَّافِعُ", "Ar-Rafi'", "The Exalter of the Humble", "بلند درجے عطا فرمانے والا", "सम्मान बढ़ाने वाला", "The one who elevates the believers in honor and status.", "Surah Al-An'am (6:165)"),
        AsmaName(24, "الْمُعِزُّ", "Al-Mu'izz", "The Bestower of Honor", "عزت بخشنے والا", "सम्मान दाता", "He who grants victory, majesty, and dignity to whom He wills.", "Surah Ali 'Imran (3:26)"),
        AsmaName(25, "الْمُذِلُّ", "Al-Mudhill", "The Dishonorer of Evil", "رسوا کرنے والا", "अपमानित करने वाला", "He who humiliates oppressors and enemies of truth.", "Surah Ali 'Imran (3:26)"),
        AsmaName(26, "السَّمِيعُ", "As-Sami'", "The All-Hearing", "سب کچھ سننے والا", "सर्वश्रोता", "Who hears the silent whispers and deepest yearnings of every soul.", "Surah Al-Baqarah (2:127)"),
        AsmaName(27, "الْبَصِيرُ", "Al-Basir", "The All-Seeing", "سب کچھ دیکھنے والا", "सर्वद्रष्टा", "Who sees the black ant on the dark stone in the pitch of night.", "Surah Al-Hajj (22:75)"),
        AsmaName(28, "الْحَكَمُ", "Al-Hakam", "The Supreme Judge", "فیصلہ فرمانے والا", "परम न्यायधीश", "The supreme arbiter whose judgment is immaculate justice.", "Surah Al-An'am (6:114)"),
        AsmaName(29, "الْعَدْلُ", "Al-'Adl", "The Embodiment of Justice", "سراپا انصاف", "परम न्यायप्रिय", "The entirely just, incapable of the slightest injustice.", "Surah Al-An'am (6:115)"),
        AsmaName(30, "اللَّطِيفُ", "Al-Latif", "The Subtle and Kind", "باریک بین و مہربان", "अति सूक्ष्म एवं कृपालु", "Whose subtle benevolence reaches servants in ways unseen.", "Surah Al-Mulk (67:14)"),
        AsmaName(31, "الْخَبِيرُ", "Al-Khabir", "The All-Aware", "ہر راز سے باخبر", "सबका भेद जानने वाला", "Knowing the internal secrets and inner truths of all things.", "Surah Al-An'am (6:18)"),
        AsmaName(32, "الْحَلِيمُ", "Al-Halim", "The Most Forbearing", "بردبار اور تحمل والا", "अत्यंत सहनशील", "Who does not hasten to punish, offering boundless chance to reform.", "Surah Al-Baqarah (2:225)"),
        AsmaName(33, "الْعَظِيمُ", "Al-'Azim", "The Magnanimous / Magnificent", "بے پناہ عظمت والا", "सर्वोच्च महिमाशाली", "Whose greatness exceeds human comprehension and praise.", "Surah Al-Baqarah (2:255)"),
        AsmaName(34, "الْغَفُورُ", "Al-Ghafur", "The Most Forgiving", "بہت بخشنے والا", "परम क्षमावान", "Who forgives repeated shortcomings and wraps servants in mercy.", "Surah Fatir (35:28)"),
        AsmaName(35, "الشَّكُورُ", "Ash-Shakur", "The Appreciative of Good", "قدردان اور صلہ دینے والا", "कद्रदान, प्रतिफल दाता", "Who rewards little righteous effort with immense eternal recompense.", "Surah Fatir (35:30)")
    )

    // Sacred Hijri Events
    val sacredHijriCalendar = listOf(
        HijriDateEvent("1 Muharram", "New Hijri Year", "Islamic New Year", "نیا اسلامی سال", "नया इस्लामी साल", "رأس السنة الهجرية", "Commemorates the Hijrah of Prophet Muhammad (ﷺ) from Makkah to Madinah.", false),
        HijriDateEvent("10 Muharram", "Day of Ashura", "Day of Ashura", "یومِ عاشورہ", "यौमे आशूरा", "يوم عاشوراء", "Fasting expiates sins of the previous year. Moses (AS) saved from Pharaoh.", true),
        HijriDateEvent("12 Rabi' al-Awwal", "Seerah Reflections", "Prophet's Seerah Day", "سیرت النبی ﷺ", "सीरतुन्नबी ﷺ", "سيرة الحبيب المصطفى ﷺ", "Contemplating the blessed life, character, and legacy of Prophet Muhammad (ﷺ).", false),
        HijriDateEvent("27 Rajab", "Al-Isra wal-Mi'raj", "Night Journey & Ascension", "اسراء و معراج", "मे'राज की रात", "الإسراء والمعراج", "The divine miraculous night journey where 5 daily prayers were gifted.", false),
        HijriDateEvent("15 Sha'ban", "Nisf Sha'ban", "Night of Forgiveness", "شبِ برات", "शब-ए-बरात", "ليلة النصف من شعبان", "Preparation for Ramadan with sincere istighfar and night worship.", false),
        HijriDateEvent("1 Ramadan", "Start of Ramadan", "First Day of Fasting", "یکم رمضان المبارک", "रमज़ान का पहला रोज़ा", "غرة شهر رمضان المبارك", "The month of the Quran, fasting, repentance, and multiplied rewards.", true),
        HijriDateEvent("21–29 Ramadan", "Odd Nights", "Laylatul Qadr (Night of Power)", "شبِ قدر", "शबे क़द्र", "ليلة القدر المباركة", "A night better than a thousand months (83+ years of continuous devotion).", true),
        HijriDateEvent("1 Shawwal", "Eid-ul-Fitr", "Eid-ul-Fitr Celebration", "عید الفطر المبارک", "ईद-उल-फ़ित्र", "عيد الفطر المبارك", "Day of thankfulness after completing the sacred month of fasting.", false),
        HijriDateEvent("8–12 Dhul Hijjah", "Sacred Hajj", "Days of Hajj", "ایامِ حج و طواف", "हज्ज के दिन", "أيام الحج العظام", "The greatest pilgrimage to the Kaaba, Mina, Muzdalifah, and Arafah.", false),
        HijriDateEvent("9 Dhul Hijjah", "Day of Arafah", "Day of Arafah", "یومِ عرفہ", "यौमे अरफ़ा", "يوم عرفة", "Fasting expiates sins of the previous and upcoming year for non-pilgrims.", true),
        HijriDateEvent("10 Dhul Hijjah", "Eid-ul-Adha", "Eid-ul-Adha & Qurbani", "عید الاضحیٰ", "ईद-उल-अज़हा", "عيد الأضحى المبارك", "Commemorating the profound sacrifice of Prophet Ibrahim (AS).", false)
    )
}
