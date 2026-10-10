package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mosque
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.viewmodel.NavigationTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoorDeenTopAppBar(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onOpenAiChat: (() -> Unit)? = null,
    onOpenProfile: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier.testTag("noor_deen_top_app_bar"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_noor),
                        contentDescription = "Noor Deen Icon",
                        modifier = Modifier.size(32.dp).clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Noor Deen",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "نور الدين",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "Non-Profit • Ad-Free • Sunnah Verified",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            if (onOpenAiChat != null) {
                IconButton(
                    onClick = onOpenAiChat,
                    modifier = Modifier.testTag("top_bar_ai_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ask AI Scholar",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (onOpenProfile != null) {
                IconButton(
                    onClick = onOpenProfile,
                    modifier = Modifier.testTag("top_bar_profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Profile & Account",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Language selector button
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { showLanguageDialog = true }
                    .testTag("language_selector_button"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentLanguage.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = "Select Language / اختر اللغة",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    Text(
                        text = "Adjusts text, translations, and layout direction (RTL/LTR).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AppLanguage.entries.forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onLanguageSelected(lang)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        ) {
                            RadioButton(
                                selected = lang == currentLanguage,
                                onClick = {
                                    onLanguageSelected(lang)
                                    showLanguageDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = lang.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (lang.isRtl) "Right-to-Left (RTL)" else "Left-to-Right (LTR)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun NoorDeenBottomNavigation(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val homeLabel = when (currentLanguage) {
        AppLanguage.ARABIC -> "الرئيسية"
        AppLanguage.URDU -> "صفحۂ اول"
        AppLanguage.HINDI -> "होम"
        AppLanguage.ENGLISH -> "Home"
    }

    val quranLabel = when (currentLanguage) {
        AppLanguage.ARABIC -> "المصحف"
        AppLanguage.URDU -> "قرآن"
        AppLanguage.HINDI -> "क़ुरआन"
        AppLanguage.ENGLISH -> "Quran"
    }

    val prayerLabel = when (currentLanguage) {
        AppLanguage.ARABIC -> "الصلاة"
        AppLanguage.URDU -> "نماز"
        AppLanguage.HINDI -> "नमाज़"
        AppLanguage.ENGLISH -> "Prayer"
    }

    val hadithLabel = when (currentLanguage) {
        AppLanguage.ARABIC -> "الحديث"
        AppLanguage.URDU -> "حدیث"
        AppLanguage.HINDI -> "हदीस"
        AppLanguage.ENGLISH -> "Hadith"
    }

    val toolsLabel = when (currentLanguage) {
        AppLanguage.ARABIC -> "الأدوات"
        AppLanguage.URDU -> "وسائل"
        AppLanguage.HINDI -> "टूल्स"
        AppLanguage.ENGLISH -> "More"
    }

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("noor_deen_bottom_navigation"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == NavigationTab.HOME,
            onClick = { onTabSelected(NavigationTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = homeLabel
                )
            },
            label = { Text(homeLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_home")
        )

        NavigationBarItem(
            selected = currentTab == NavigationTab.QURAN,
            onClick = { onTabSelected(NavigationTab.QURAN) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.QURAN) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                    contentDescription = quranLabel
                )
            },
            label = { Text(quranLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_quran")
        )

        NavigationBarItem(
            selected = currentTab == NavigationTab.PRAYER,
            onClick = { onTabSelected(NavigationTab.PRAYER) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.PRAYER) Icons.Filled.Mosque else Icons.Outlined.Mosque,
                    contentDescription = prayerLabel
                )
            },
            label = { Text(prayerLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_prayer")
        )

        NavigationBarItem(
            selected = currentTab == NavigationTab.HADITH,
            onClick = { onTabSelected(NavigationTab.HADITH) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.HADITH) Icons.Filled.VolunteerActivism else Icons.Outlined.VolunteerActivism,
                    contentDescription = hadithLabel
                )
            },
            label = { Text(hadithLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_hadith")
        )

        NavigationBarItem(
            selected = currentTab == NavigationTab.TOOLS,
            onClick = { onTabSelected(NavigationTab.TOOLS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.TOOLS) Icons.Filled.Category else Icons.Outlined.Category,
                    contentDescription = toolsLabel
                )
            },
            label = { Text(toolsLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_tools")
        )
    }
}
