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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

@Composable
fun ToolsScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    when (currentSubScreen) {
        SubScreen.ASMA_UL_HUSNA -> AsmaUlHusnaScreen(viewModel = viewModel, modifier = modifier)
        SubScreen.ZAKAT_CALCULATOR -> ZakatCalculatorScreen(viewModel = viewModel, modifier = modifier)
        SubScreen.RAMADAN_KHATAM -> RamadanKhatamScreen(viewModel = viewModel, modifier = modifier)
        SubScreen.DIGITAL_TASBEEH -> DigitalTasbeehScreen(viewModel = viewModel, modifier = modifier)
        SubScreen.HIJRI_CALENDAR -> HijriCalendarScreen(viewModel = viewModel, modifier = modifier)
        else -> ToolsMenuListView(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun ToolsMenuListView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("tools_menu_list_view")
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Islamic Life Tools & Utilities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Authentic calculation tools without commercial ads or subscriptions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                ToolMenuCard(
                    title = "Noor Deen AI Scholar",
                    subtitle = "Answers strictly with Quran & Sahih Hadith citations",
                    arabicTitle = "عالم نور الدين",
                    icon = Icons.Default.AutoAwesome,
                    tag = "tool_ai_scholar",
                    onClick = { viewModel.openSubScreen(SubScreen.AI_SCHOLAR_CHAT) }
                )
            }

            item {
                ToolMenuCard(
                    title = "99 Names of Allah (Asma-ul-Husna)",
                    subtitle = "Meanings, spiritual reflections & flashcard memorization",
                    arabicTitle = "أسماء الله الحسنى",
                    icon = Icons.Default.Stars,
                    tag = "tool_asma_ul_husna",
                    onClick = { viewModel.openSubScreen(SubScreen.ASMA_UL_HUSNA) }
                )
            }

            item {
                ToolMenuCard(
                    title = "Zakat Calculator",
                    subtitle = "Multi-asset evaluation (Cash, Gold, Silver, Stocks) & Nisab",
                    arabicTitle = "حاسبة الزكاة",
                    icon = Icons.Default.Calculate,
                    tag = "tool_zakat_calculator",
                    onClick = { viewModel.openSubScreen(SubScreen.ZAKAT_CALCULATOR) }
                )
            }

            item {
                ToolMenuCard(
                    title = "Hijri Calendar & Sacred Events",
                    subtitle = "Dual calendar, Ashura, Arafah, White Days fasting",
                    arabicTitle = "التقويم الهجري",
                    icon = Icons.Default.CalendarMonth,
                    tag = "tool_hijri_calendar",
                    onClick = { viewModel.openSubScreen(SubScreen.HIJRI_CALENDAR) }
                )
            }

            item {
                ToolMenuCard(
                    title = "Ramadan & Khatam Companion",
                    subtitle = "Daily fasting tracker & customized Quran completion plans",
                    arabicTitle = "رفيق رمضان وختم القرآن",
                    icon = Icons.Default.NightsStay,
                    tag = "tool_ramadan_khatam",
                    onClick = { viewModel.openSubScreen(SubScreen.RAMADAN_KHATAM) }
                )
            }

            item {
                ToolMenuCard(
                    title = "Digital Tasbeeh Counter",
                    subtitle = "Haptic feedback beads with Sunnah Dhikr target limits",
                    arabicTitle = "المسبحة الإلكترونية",
                    icon = Icons.Default.Fingerprint,
                    tag = "tool_digital_tasbeeh",
                    onClick = { viewModel.openSubScreen(SubScreen.DIGITAL_TASBEEH) }
                )
            }
        }
    }
}

@Composable
fun ToolMenuCard(
    title: String,
    subtitle: String,
    arabicTitle: String,
    icon: ImageVector,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(tag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = arabicTitle,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
