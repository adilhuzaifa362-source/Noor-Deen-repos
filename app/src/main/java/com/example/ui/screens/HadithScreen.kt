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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.RecordVoiceOver
import com.example.model.AudioPlaybackStatus
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AppLanguage
import com.example.model.Hadith
import com.example.model.HadithTopic
import com.example.service.HadithRepository
import com.example.service.HadithSemanticEngine
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

@Composable
fun HadithScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedBook by viewModel.selectedHadithBook.collectAsState()
    val selectedTopic by viewModel.selectedHadithTopic.collectAsState()
    val selectedNarrator by viewModel.selectedNarrator.collectAsState()
    val searchQuery by viewModel.hadithSearchQuery.collectAsState()
    val filteredHadiths by viewModel.filteredHadiths.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val activeTrack by viewModel.activeAudioTrack.collectAsState()
    val audioStatus by viewModel.audioPlaybackStatus.collectAsState()

    var activeDisplayLang by remember { mutableStateOf(currentLanguage) }
    var selectedHadithDetail by remember { mutableStateOf<Hadith?>(null) }
    var browseTab by remember { mutableIntStateOf(0) } // 0: Browse by Book, 1: Browse by Category, 2: Semantic Search

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("hadith_screen_view")
    ) {
        // Top Header Banner with AI Scholar Shortcut
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Hadith",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Verified Hadith Repository",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Authentic narrations from Sahih Sitta (الكتب الستة)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Open AI Scholar Chatbot Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .clickable { viewModel.openSubScreen(SubScreen.AI_SCHOLAR_CHAT) }
                            .testTag("open_ai_scholar_chat_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Scholar",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Browse Mode Tab Selector (Book vs Category vs Semantic Search)
        TabRow(
            selectedTabIndex = browseTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Tab(
                selected = browseTab == 0,
                onClick = { browseTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Book, contentDescription = "Book", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("By Book", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
            Tab(
                selected = browseTab == 1,
                onClick = { browseTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Category, contentDescription = "Category", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("By Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
            Tab(
                selected = browseTab == 2,
                onClick = { browseTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Search", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Contextual Controls depending on Tab
        when (browseTab) {
            0 -> {
                // BROWSE BY BOOK (Sahih Sitta canonical collections)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedBook == null || selectedBook == "All Books",
                            onClick = { viewModel.setHadithBook("All Books") },
                            label = { Text("All 6 Books") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("filter_book_all")
                        )
                    }
                    items(HadithRepository.sahihSittaBooks) { bookName ->
                        val isSelected = selectedBook == bookName
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setHadithBook(bookName) },
                            label = { Text(bookName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("filter_book_${bookName.replace(" ", "_")}")
                        )
                    }
                }
            }
            1 -> {
                // BROWSE BY CATEGORY / TOPIC
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedTopic == null,
                            onClick = { viewModel.setHadithTopic(null) },
                            label = { Text("All Categories") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("filter_topic_all")
                        )
                    }
                    items(HadithTopic.entries) { topic ->
                        val topicTitle = when (currentLanguage) {
                            AppLanguage.URDU -> topic.displayNameUr
                            AppLanguage.HINDI -> topic.displayNameHi
                            AppLanguage.ARABIC -> topic.displayNameAr
                            else -> topic.displayNameEn
                        }
                        FilterChip(
                            selected = selectedTopic == topic,
                            onClick = { viewModel.setHadithTopic(topic) },
                            label = { Text(topicTitle) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("filter_topic_${topic.name}")
                        )
                    }
                }
            }
            2 -> {
                // SEARCH BAR
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setHadithSearchQuery(it) },
                    placeholder = { Text("Search by meaning e.g. 'kindness', 'intentions', 'fasting'...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setHadithSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .testTag("hadith_search_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Narrator Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            items(HadithSemanticEngine.allNarrators) { narrator ->
                FilterChip(
                    selected = selectedNarrator == narrator,
                    onClick = { viewModel.setHadithNarrator(narrator) },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Narrator", modifier = Modifier.size(14.dp))
                    },
                    label = { Text(narrator) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        // Language Selector Chips for Matn / Translation View & Count Badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredHadiths.size} Sahih Hadiths found",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Language",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                AppLanguage.entries.forEach { lang ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (activeDisplayLang == lang) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { activeDisplayLang = lang }
                    ) {
                        Text(
                            text = lang.code.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeDisplayLang == lang) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // Hadith Narrations List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 6.dp, bottom = 80.dp)
        ) {
            if (filteredHadiths.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No matching Hadiths found.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try clearing search filters or selecting 'All Books' or 'All Categories'.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {
                                viewModel.setHadithBook("All Books")
                                viewModel.setHadithTopic(null)
                                viewModel.setHadithNarrator("All Narrators")
                                viewModel.setHadithSearchQuery("")
                            }) {
                                Text("Reset All Filters")
                            }
                        }
                    }
                }
            }

            items(filteredHadiths) { hadith ->
                val isThisHadithPlaying = (audioStatus == AudioPlaybackStatus.PLAYING || audioStatus == AudioPlaybackStatus.BUFFERING) &&
                    (activeTrack?.id == "hadith_${hadith.id}" || (activeTrack?.isTts == true && activeTrack?.title?.contains(hadith.hadithNumber) == true))

                HadithCard(
                    hadith = hadith,
                    activeLang = activeDisplayLang,
                    isPlaying = isThisHadithPlaying,
                    onPlayArabic = {
                        if (isThisHadithPlaying) viewModel.pauseAudio() else viewModel.playHadithAudio(hadith, playArabic = true)
                    },
                    onPlayTranslation = {
                        viewModel.playHadithAudio(hadith, playArabic = false)
                    },
                    onClick = { selectedHadithDetail = hadith }
                )
            }
        }
    }

    // Hadith Full Detail Modal Dialog
    if (selectedHadithDetail != null) {
        val h = selectedHadithDetail!!
        AlertDialog(
            onDismissRequest = { selectedHadithDetail = null },
            modifier = Modifier.testTag("hadith_detail_dialog"),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = h.book,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${h.hadithNumber} • ${h.chapter}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = h.grading,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        val isDetailPlaying = (audioStatus == AudioPlaybackStatus.PLAYING || audioStatus == AudioPlaybackStatus.BUFFERING) &&
                            (activeTrack?.id == "hadith_${h.id}" || (activeTrack?.isTts == true && activeTrack?.title?.contains(h.hadithNumber) == true))

                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("hadith_dialog_audio_bar"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (isDetailPlaying) viewModel.pauseAudio() else viewModel.playHadithAudio(h, playArabic = true)
                                            },
                                            modifier = Modifier.testTag("hadith_dialog_play_arabic")
                                        ) {
                                            Icon(
                                                imageVector = if (isDetailPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = "Play Arabic Narration",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isDetailPlaying) "Streaming Audio" else "Arabic Narration",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Authentic Islamic Audio",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.playHadithAudio(h, playArabic = false) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.testTag("hadith_dialog_play_translation")
                                ) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = "Voice", tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Voice (${activeDisplayLang.code.uppercase()})", style = MaterialTheme.typography.labelSmall, color = Color.White)
                                }
                            }
                        }
                    }

                    item {
                        // Sanad Chain
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = h.sanad,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    item {
                        // Arabic Matn Text
                        Text(
                            text = h.matnArabic,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "English Translation:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = h.matnEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    item {
                        Text(
                            text = "اردو ترجمہ (Urdu Translation):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = h.matnUr,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    item {
                        Text(
                            text = "हिन्दी अनुवाद (Hindi Translation):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = h.matnHi,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedHadithDetail = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun HadithCard(
    hadith: Hadith,
    activeLang: AppLanguage,
    isPlaying: Boolean = false,
    onPlayArabic: () -> Unit = {},
    onPlayTranslation: () -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("hadith_card_${hadith.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Info Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = hadith.book,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${hadith.hadithNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = hadith.grading,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic Matn
            Text(
                text = hadith.matnArabic,
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Translated Text based on Active Language Selection
            val translationText = when (activeLang) {
                AppLanguage.URDU -> hadith.matnUr
                AppLanguage.HINDI -> hadith.matnHi
                AppLanguage.ARABIC -> hadith.matnEn
                else -> hadith.matnEn
            }

            Text(
                text = translationText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer with Narrator, Audio Play Buttons & Read Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Narrator: ${hadith.narrator}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPlayArabic,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("play_hadith_arabic_${hadith.id}")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                            contentDescription = "Listen Arabic Narration",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    IconButton(
                        onClick = onPlayTranslation,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("play_hadith_translation_${hadith.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Listen Translation",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Read →",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
