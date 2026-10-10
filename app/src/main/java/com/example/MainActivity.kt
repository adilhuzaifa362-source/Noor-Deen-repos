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
import com.example.ui.screens.DuasScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.PrayerScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.NoorDeenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    onOpenAiChat = { viewModel.openSubScreen(com.example.viewmodel.SubScreen.AI_SCHOLAR_CHAT) }
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
                if (currentSubScreen == com.example.viewmodel.SubScreen.AI_SCHOLAR_CHAT) {
                    com.example.ui.screens.AiScholarChatScreen(viewModel = viewModel)
                } else {
                    when (currentTab) {
                        NavigationTab.QURAN -> QuranScreen(viewModel = viewModel)
                        NavigationTab.DUAS -> DuasScreen(viewModel = viewModel)
                        NavigationTab.PRAYER -> PrayerScreen(viewModel = viewModel)
                        NavigationTab.HADITH -> HadithScreen(viewModel = viewModel)
                        NavigationTab.TOOLS -> ToolsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
