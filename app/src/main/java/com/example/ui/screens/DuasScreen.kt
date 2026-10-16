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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import com.example.model.AudioPlaybackStatus
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IslamicData
import com.example.model.AppLanguage
import com.example.model.DuaCategory
import com.example.model.Supplication
import com.example.ui.components.TajweedAnnotatedText
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

@Composable
fun DuasScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    if (currentSubScreen == SubScreen.DUA_DETAIL) {
        DuaDetailReaderView(viewModel = viewModel, modifier = modifier)
    } else {
        DuaListView(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun DuaListView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedDuaCategory.collectAsState()
    val searchQuery by viewModel.duaSearchQuery.collectAsState()
    val supplicationCounts by viewModel.supplicationCounts.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedDuaIds.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val activeTrack by viewModel.activeAudioTrack.collectAsState()
    val audioStatus by viewModel.audioPlaybackStatus.collectAsState()

    val filteredDuas = remember(selectedCategory, searchQuery) {
        IslamicData.supplications.filter { dua ->
            val matchCategory = selectedCategory == null || dua.category == selectedCategory
            val matchSearch = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                dua.titleEn.lowercase().contains(q) ||
                dua.titleUr.contains(q) ||
                dua.titleHi.contains(q) ||
                dua.arabicText.contains(q) ||
                dua.translationEn.lowercase().contains(q)
            }
            matchCategory && matchSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("duas_list_view")
    ) {
        // Top Header with Back Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("duas_list_back_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Duas & Azkar (الأدعية والأذكار)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Authentic Masnoon Duas from the Sunnah & Hisnul Muslim",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setDuaSearchQuery(it) },
            placeholder = { Text("Search Duas, Wazifas, Adhkar...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("dua_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Categories Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.filterDuaCategory(null) },
                    label = { Text("All Supplications") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            items(DuaCategory.entries) { cat ->
                val title = when (currentLanguage) {
                    AppLanguage.URDU -> cat.titleUr
                    AppLanguage.HINDI -> cat.titleHi
                    AppLanguage.ARABIC -> cat.titleAr
                    else -> cat.titleEn
                }
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { viewModel.filterDuaCategory(cat) },
                    label = { Text(title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        // Dua Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredDuas) { dua ->
                val count = supplicationCounts[dua.id] ?: 0
                val isBookmarked = bookmarkedIds.contains(dua.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openDuaDetail(dua) }
                        .testTag("dua_card_${dua.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                val title = when (currentLanguage) {
                                    AppLanguage.URDU -> dua.titleUr
                                    AppLanguage.HINDI -> dua.titleHi
                                    AppLanguage.ARABIC -> dua.titleAr
                                    else -> dua.titleEn
                                }
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = dua.reference,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val isThisDuaPlaying = (audioStatus == AudioPlaybackStatus.PLAYING || audioStatus == AudioPlaybackStatus.BUFFERING) &&
                                    (activeTrack?.id == "dua_${dua.id}" || (activeTrack?.isTts == true && activeTrack?.title == dua.titleEn))

                                IconButton(
                                    onClick = {
                                        if (isThisDuaPlaying) {
                                            viewModel.pauseAudio()
                                        } else {
                                            viewModel.playDuaAudio(dua)
                                        }
                                    },
                                    modifier = Modifier.size(32.dp).testTag("play_dua_audio_${dua.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isThisDuaPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                                        contentDescription = "Listen Arabic",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.playDuaTranslationTts(dua, currentLanguage) },
                                    modifier = Modifier.size(32.dp).testTag("speak_dua_translation_${dua.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RecordVoiceOver,
                                        contentDescription = "Listen Translation Voice",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.toggleBookmarkDua(dua.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isBookmarked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Short Arabic Preview
                        Text(
                            text = dua.arabicText,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Translation preview
                        val trans = when (currentLanguage) {
                            AppLanguage.URDU -> dua.translationUr
                            AppLanguage.HINDI -> dua.translationHi
                            else -> dua.translationEn
                        }
                        Text(
                            text = trans,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tap Counter Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "Target: ${dua.targetCount}x",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$count done",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (count >= dua.targetCount) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Tap Button
                            Card(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.incrementDuaCount(dua.id, dua.targetCount) }
                                    .testTag("tap_count_button_${dua.id}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (count >= dua.targetCount)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (count >= dua.targetCount) Icons.Default.CheckCircle else Icons.Default.TouchApp,
                                        contentDescription = "Count",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (count >= dua.targetCount) "Complete" else "Tap Count",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
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

@Composable
fun DuaDetailReaderView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDuaState by viewModel.selectedDua.collectAsState()
    val dua = selectedDuaState ?: IslamicData.supplications.first()
    val supplicationCounts by viewModel.supplicationCounts.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedDuaIds.collectAsState()
    val isTajweedEnabled by viewModel.isTajweedEnabled.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val activeTrack by viewModel.activeAudioTrack.collectAsState()
    val audioStatus by viewModel.audioPlaybackStatus.collectAsState()

    val count = supplicationCounts[dua.id] ?: 0
    val isBookmarked = bookmarkedIds.contains(dua.id)
    val isCompleted = count >= dua.targetCount
    val isThisDuaPlaying = (audioStatus == AudioPlaybackStatus.PLAYING || audioStatus == AudioPlaybackStatus.BUFFERING) &&
        (activeTrack?.id == "dua_${dua.id}" || (activeTrack?.isTts == true && activeTrack?.title == dua.titleEn))

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("dua_detail_reader_view")
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("dua_reader_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = dua.titleEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dua.reference,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = { viewModel.toggleBookmarkDua(dua.id) }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // In-page Audio Player Bar for Dua Recitation & Translation Speech
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("dua_inpage_audio_player"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (isThisDuaPlaying) {
                                        viewModel.pauseAudio()
                                    } else {
                                        viewModel.playDuaAudio(dua)
                                    }
                                },
                                modifier = Modifier.testTag("dua_detail_play_arabic_button")
                            ) {
                                Icon(
                                    imageVector = if (isThisDuaPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play Arabic Recitation",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isThisDuaPlaying) "Streaming Arabic Recitation" else "Arabic Recitation",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (dua.audioUrl != null) "Authentic Qari Recitation (EveryAyah)" else "Sunnah Phonetic Pronunciation",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }

                    // Multilingual Translation TTS Voice Button
                    Button(
                        onClick = { viewModel.playDuaTranslationTts(dua, currentLanguage) },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("dua_detail_listen_translation_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Listen Translation Voice",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val langLabel = when (currentLanguage) {
                            AppLanguage.URDU -> "اردو"
                            AppLanguage.HINDI -> "हिंदी"
                            else -> "English"
                        }
                        Text(
                            text = "Voice ($langLabel)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Scrollable Reader Body
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Arabic Calligraphy Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        TajweedAnnotatedText(
                            text = dua.arabicText,
                            spans = dua.tajweedSpans,
                            isTajweedEnabled = isTajweedEnabled,
                            fontSize = 28,
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }

            // Transliteration Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Transliteration",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dua.transliteration,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Multi-Language Translation Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Translation",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // English
                        Text(
                            text = "English:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dua.translationEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Urdu
                        Text(
                            text = "اردو ترجمہ:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dua.translationUr,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hindi
                        Text(
                            text = "हिन्दी अनुवाद:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = dua.translationHi,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Virtues & Authentic Hadith Context
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Virtues & Sunnah Guidance",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dua.virtue,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Giant Tap Counter Zone
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.incrementDuaCount(dua.id, dua.targetCount) }
                        .testTag("big_tap_counter_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCompleted)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isCompleted) "Target Completed!" else "Tap Anywhere To Count",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "$count",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "of ${dua.targetCount} Sunnah Target",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { (count.toFloat() / dua.targetCount).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(8.dp)
                                .clip(CircleShape),
                            color = if (isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        IconButton(
                            onClick = { viewModel.resetDuaCount(dua.id) },
                            modifier = Modifier.testTag("reset_dua_count_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Count")
                        }
                    }
                }
            }
        }
    }
}
