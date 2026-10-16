package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.ui.components.NoorDeenBottomNavigation
import com.example.ui.components.NoorDeenTopAppBar
import com.example.ui.screens.AiScholarChatScreen
import com.example.ui.screens.AsmaUlHusnaScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DigitalTasbeehScreen
import com.example.ui.screens.DuasScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.HijriCalendarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MasjidNetworkScreen
import com.example.ui.screens.PrayerScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.RamadanKhatamScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.ZakatCalculatorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.service.AudioPlaybackManager.initialize(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NoorDeenApp()
            }
        }
    }
}

@Composable
fun NoorDeenApp(
    viewModel: NoorDeenViewModel = viewModel()
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    // Handle Android system back press for sub-screens
    BackHandler(enabled = currentSubScreen != null) {
        viewModel.navigateBack()
    }

    val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                NoorDeenTopAppBar(
                    currentLanguage = currentLanguage,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onOpenAiChat = { viewModel.openSubScreen(SubScreen.AI_SCHOLAR_CHAT) },
                    onOpenProfile = { viewModel.openSubScreen(SubScreen.AUTH_PROFILE) }
                )
            },
            bottomBar = {
                NoorDeenBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    currentLanguage = currentLanguage
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (currentSubScreen != null) {
                    when (currentSubScreen) {
                        SubScreen.AI_SCHOLAR_CHAT -> AiScholarChatScreen(viewModel = viewModel)
                        SubScreen.AUTH_PROFILE -> AuthScreen(viewModel = viewModel)
                        SubScreen.QIBLA_COMPASS -> QiblaScreen(viewModel = viewModel)
                        SubScreen.MASJID_NETWORK -> MasjidNetworkScreen(viewModel = viewModel)
                        SubScreen.DIGITAL_TASBEEH -> DigitalTasbeehScreen(viewModel = viewModel)
                        SubScreen.ZAKAT_CALCULATOR -> ZakatCalculatorScreen(viewModel = viewModel)
                        SubScreen.ASMA_UL_HUSNA -> AsmaUlHusnaScreen(viewModel = viewModel)
                        SubScreen.RAMADAN_KHATAM -> RamadanKhatamScreen(viewModel = viewModel)
                        SubScreen.HIJRI_CALENDAR -> HijriCalendarScreen(viewModel = viewModel)
                        SubScreen.DUAS_AND_AZKAR -> DuasScreen(viewModel = viewModel)
                        SubScreen.DUA_DETAIL -> DuasScreen(viewModel = viewModel)
                        SubScreen.SURAH_READER -> QuranScreen(viewModel = viewModel)
                        null -> Unit
                    }
                } else {
                    when (currentTab) {
                        NavigationTab.HOME -> HomeScreen(viewModel = viewModel)
                        NavigationTab.QURAN -> QuranScreen(viewModel = viewModel)
                        NavigationTab.PRAYER -> PrayerScreen(viewModel = viewModel)
                        NavigationTab.HADITH -> HadithScreen(viewModel = viewModel)
                        NavigationTab.TOOLS -> ToolsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
