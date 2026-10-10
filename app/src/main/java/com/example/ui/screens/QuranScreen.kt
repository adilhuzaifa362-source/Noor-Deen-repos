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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var showTajweedGuideDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().testTag("surah_reader_view")) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
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
                            Column {
                                Text(
                                    text = "${surah.nameEn} (${surah.nameArabic})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${surah.revelationType.name} • ${surah.totalAyahs} Verses",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // View controls
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bismillah Banner (For Surahs other than At-Tawbah)
                if (surah.number != 9) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
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

                val verses = if (surah.verses.isNotEmpty()) surah.verses else IslamicData.fatihaVerses

                items(verses) { verse ->
                    val isHighlighted = isPlayingAudio && currentAyah == verse.ayahNumber

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
                        if (isSideBySide) {
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
                                    val transText = when (currentLanguage) {
                                        AppLanguage.URDU -> verse.translationUr
                                        AppLanguage.HINDI -> verse.translationHi
                                        AppLanguage.ARABIC -> verse.transliteration
                                        else -> verse.translationEn
                                    }
                                    Text(
                                        text = transText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
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

                                Spacer(modifier = Modifier.height(10.dp))

                                // Transliteration
                                Text(
                                    text = verse.transliteration,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Active Translation
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
