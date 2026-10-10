package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IslamicData
import com.example.model.AppLanguage
import com.example.model.RevelationType
import com.example.model.Surah
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.TajweedAnnotatedText
import com.example.ui.components.TajweedLegendBar
import com.example.ui.components.TajweedLegendDialog
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

@Composable
fun QuranScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    if (currentSubScreen == SubScreen.SURAH_READER) {
        SurahReaderView(viewModel = viewModel, modifier = modifier)
    } else {
        SurahIndexView(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun SurahIndexView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            IslamicData.allSurahs
        } else {
            val q = searchQuery.trim().lowercase()
            IslamicData.allSurahs.filter {
                it.number.toString() == q ||
                it.nameEn.lowercase().contains(q) ||
                it.nameArabic.contains(q) ||
                it.nameUr.contains(q) ||
                it.nameHi.contains(q) ||
                it.meaningEn.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("surah_index_view")
    ) {
        // Hero Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "The Holy Quran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = "15-Line Uthmani Script • Hafs 'an 'Asim",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                            )
                        }
                        Text(
                            text = "القرآن الكريم",
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        // Quick Surah Shortcuts
        Text(
            text = "Frequently Read Surahs",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val quickList = listOf(
                IslamicData.allSurahs.first { it.number == 1 }, // Al-Fatihah
                IslamicData.allSurahs.first { it.number == 18 }, // Al-Kahf
                IslamicData.allSurahs.first { it.number == 36 }, // Yasin
                IslamicData.allSurahs.first { it.number == 55 }, // Ar-Rahman
                IslamicData.allSurahs.first { it.number == 67 }, // Al-Mulk
                IslamicData.allSurahs.first { it.number == 112 } // Al-Ikhlas
            )
            items(quickList) { surah ->
                Card(
                    modifier = Modifier
                        .clickable { viewModel.openSurah(surah) }
                        .testTag("quick_surah_${surah.number}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah.nameArabic,
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = surah.nameEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, number, or translation...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("surah_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Surah List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredSurahs) { surah ->
                SurahItemCard(
                    surah = surah,
                    currentLanguage = currentLanguage,
                    onClick = { viewModel.openSurah(surah) }
                )
            }
        }
    }
}

@Composable
fun SurahItemCard(
    surah: Surah,
    currentLanguage: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("surah_card_${surah.number}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Surah Number Diamond/Circle
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    val displayName = when (currentLanguage) {
                        AppLanguage.URDU -> surah.nameUr
                        AppLanguage.HINDI -> surah.nameHi
                        else -> surah.nameEn
                    }
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = surah.meaningEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${surah.totalAyahs} Ayahs",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (surah.revelationType == RevelationType.MAKKI)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (surah.revelationType == RevelationType.MAKKI) "Makki" else "Madani",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (surah.revelationType == RevelationType.MAKKI)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Arabic Name
            Text(
                text = surah.nameArabic,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SurahReaderView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val surah by viewModel.selectedSurah.collectAsState()
    val isSideBySide by viewModel.isSideBySideView.collectAsState()
    val isTajweedEnabled by viewModel.isTajweedEnabled.collectAsState()
    val selectedLegendRule by viewModel.selectedTajweedLegend.collectAsState()
    val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()
    val currentAyah by viewModel.currentPlayingAyah.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val audioProgress by viewModel.audioProgress.collectAsState()
    val selectedQari by viewModel.selectedQari.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val selectedAyahIndex by viewModel.selectedAyahIndex.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var showTajweedGuideDialog by remember { mutableStateOf(false) }
    var showAyahSelectorDialog by remember { mutableStateOf(false) }
    var readerTranslationMode by remember { mutableStateOf("DEFAULT") } // "DEFAULT", "EN", "UR", "HI", "ALL", "AR_ONLY"

    val verses = if (surah.verses.isNotEmpty()) surah.verses else IslamicData.fatihaVerses

    Box(modifier = modifier.fillMaxSize().testTag("surah_reader_view")) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar & Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Top row: Back, Prev Surah, Title, Next Surah, Tajweed, View
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.navigateBack() },
                                modifier = Modifier.testTag("reader_back_button")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }

                            // Previous Surah Button
                            IconButton(
                                onClick = { viewModel.selectPrevSurah() },
                                modifier = Modifier.size(32.dp).testTag("reader_prev_surah_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous Surah",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Column {
                                Text(
                                    text = "${surah.number}. ${surah.nameEn} (${surah.nameArabic})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${if (surah.revelationType == RevelationType.MAKKI) "Makki" else "Madani"} • ${surah.totalAyahs} Ayahs",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Right Controls: Next Surah, Ayah Selector, Tajweed, Layout
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Next Surah Button
                            IconButton(
                                onClick = { viewModel.selectNextSurah() },
                                modifier = Modifier.size(32.dp).testTag("reader_next_surah_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next Surah",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Jump to Ayah Dialog Button
                            IconButton(
                                onClick = { showAyahSelectorDialog = true },
                                modifier = Modifier.testTag("jump_to_ayah_dialog_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatListNumbered,
                                    contentDescription = "Select Ayah",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Tajweed Toggle
                            IconButton(
                                onClick = { viewModel.toggleTajweed() },
                                modifier = Modifier.testTag("toggle_tajweed_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ColorLens,
                                    contentDescription = "Toggle Tajweed",
                                    tint = if (isTajweedEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            }

                            // Side-by-side vs Verse-by-verse toggle
                            IconButton(
                                onClick = { viewModel.toggleSideBySideView() },
                                modifier = Modifier.testTag("toggle_side_by_side_button")
                            ) {
                                Icon(
                                    imageVector = if (isSideBySide) Icons.Default.ViewColumn else Icons.Default.ViewAgenda,
                                    contentDescription = "Toggle View Layout",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Multilingual Translation Selector Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Translation",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )

                        val transOptions = listOf(
                            "DEFAULT" to "App Language",
                            "EN" to "English",
                            "UR" to "اردو (Urdu)",
                            "HI" to "हिन्दी (Hindi)",
                            "ALL" to "All 3 Langs",
                            "AR_ONLY" to "Arabic Only"
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(transOptions) { (key, label) ->
                                val isSelected = readerTranslationMode == key
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { readerTranslationMode = key }
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Surah Horizontal Quick Carousel
                    Text(
                        text = "Scroll Surahs:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(IslamicData.allSurahs) { s ->
                            val isCurrent = s.number == surah.number
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.openSurah(s)
                                        coroutineScope.launch { listState.scrollToItem(0) }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${s.number}. ${s.nameEn}",
                                        fontSize = 11.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = s.nameArabic,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }

                    // Quick Ayah Selector Strip
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ayah:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(verses) { v ->
                                val isSelectedAyah = selectedAyahIndex == v.ayahNumber
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelectedAyah) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            viewModel.jumpToAyah(v.ayahNumber)
                                            coroutineScope.launch {
                                                val targetIndex = (v.ayahNumber).coerceIn(0, verses.size)
                                                listState.animateScrollToItem(targetIndex)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${v.ayahNumber}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelectedAyah) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Tajweed Guide Bar
                    if (isTajweedEnabled) {
                        Spacer(modifier = Modifier.height(4.dp))
                        TajweedLegendBar(
                            onRuleClick = { viewModel.showTajweedLegend(it) }
                        )
                    }
                }
            }

            // Verses Scrollable Area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bismillah Banner (For Surahs other than At-Tawbah)
                if (surah.number != 9) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(verses) { verse ->
                    val isHighlighted = (isPlayingAudio && currentAyah == verse.ayahNumber) || (selectedAyahIndex == verse.ayahNumber)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verse_item_${verse.ayahNumber}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isHighlighted)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 4.dp else 1.dp)
                    ) {
                        if (isSideBySide && readerTranslationMode != "AR_ONLY") {
                            // Side-by-Side Layout
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Left: Translation
                                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                    Text(
                                        text = "Ayah ${verse.ayahNumber}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    when (readerTranslationMode) {
                                        "EN" -> Text(verse.translationEn, style = MaterialTheme.typography.bodyMedium)
                                        "UR" -> Text(verse.translationUr, style = MaterialTheme.typography.bodyMedium)
                                        "HI" -> Text(verse.translationHi, style = MaterialTheme.typography.bodyMedium)
                                        "ALL" -> {
                                            Text(verse.translationEn, style = MaterialTheme.typography.bodySmall)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(verse.translationUr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(verse.translationHi, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                        }
                                        else -> {
                                            val transText = when (currentLanguage) {
                                                AppLanguage.URDU -> verse.translationUr
                                                AppLanguage.HINDI -> verse.translationHi
                                                AppLanguage.ARABIC -> verse.transliteration
                                                else -> verse.translationEn
                                            }
                                            Text(transText, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }

                                // Right: Uthmani Arabic with Tajweed
                                Column(modifier = Modifier.weight(1.2f)) {
                                    TajweedAnnotatedText(
                                        text = verse.arabicText,
                                        spans = verse.tajweedSpans,
                                        isTajweedEnabled = isTajweedEnabled,
                                        fontSize = 22,
                                        onTajweedRuleClick = { viewModel.showTajweedLegend(it) }
                                    )
                                }
                            }
                        } else {
                            // Verse-by-Verse Layout
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                // Verse Number Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "Ayah ${verse.ayahNumber}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Arabic Uthmani Text with Tajweed
                                TajweedAnnotatedText(
                                    text = verse.arabicText,
                                    spans = verse.tajweedSpans,
                                    isTajweedEnabled = isTajweedEnabled,
                                    fontSize = 26,
                                    onTajweedRuleClick = { viewModel.showTajweedLegend(it) }
                                )

                                if (readerTranslationMode != "AR_ONLY") {
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Transliteration
                                    Text(
                                        text = verse.transliteration,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Multilingual translations rendering
                                    when (readerTranslationMode) {
                                        "EN" -> {
                                            Text(
                                                text = verse.translationEn,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        "UR" -> {
                                            Text(
                                                text = verse.translationUr,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        "HI" -> {
                                            Text(
                                                text = verse.translationHi,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        "ALL" -> {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    text = "EN: ${verse.translationEn}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = "اردو: ${verse.translationUr}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                                Text(
                                                    text = "हिन्दी: ${verse.translationHi}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                        else -> {
                                            val primaryTrans = when (currentLanguage) {
                                                AppLanguage.URDU -> verse.translationUr
                                                AppLanguage.HINDI -> verse.translationHi
                                                AppLanguage.ARABIC -> verse.translationEn
                                                else -> verse.translationEn
                                            }
                                            Text(
                                                text = primaryTrans,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Jump To Ayah Dialog
        if (showAyahSelectorDialog) {
            AlertDialog(
                onDismissRequest = { showAyahSelectorDialog = false },
                title = { Text("Jump to Ayah in ${surah.nameEn}") },
                text = {
                    Column {
                        Text(
                            text = "Select any of the ${verses.size} verses to jump directly:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(verses) { v ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (selectedAyahIndex == v.ayahNumber) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            viewModel.jumpToAyah(v.ayahNumber)
                                            coroutineScope.launch {
                                                val targetIndex = (v.ayahNumber).coerceIn(0, verses.size)
                                                listState.animateScrollToItem(targetIndex)
                                            }
                                            showAyahSelectorDialog = false
                                        }
                                ) {
                                    Text(
                                        text = "${v.ayahNumber}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedAyahIndex == v.ayahNumber) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAyahSelectorDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }

        // Bottom Synchronized Audio Player
        val totalAyahs = if (surah.verses.isNotEmpty()) surah.verses.size else 7
        AudioPlayerBar(
            surahName = surah.nameEn,
            currentAyah = currentAyah,
            totalAyahs = totalAyahs,
            isPlaying = isPlayingAudio,
            progress = audioProgress,
            playbackSpeed = playbackSpeed,
            selectedQari = selectedQari,
            onPlayPause = { viewModel.toggleAudioPlayback() },
            onPrevious = { viewModel.previousAyah() },
            onNext = { viewModel.nextAyah() },
            onSpeedChange = { viewModel.setPlaybackSpeed(it) },
            onSelectQari = { viewModel.selectQari(it) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Tajweed Legend Dialog
        TajweedLegendDialog(
            rule = selectedLegendRule,
            onDismiss = { viewModel.showTajweedLegend(null) }
        )
    }
}
