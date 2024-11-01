package com.github.bigfishcat.livingpictures.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.domain.PagesRepository
import com.github.bigfishcat.livingpictures.model.handleAction
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.BottomBarUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PopupShown
import com.github.bigfishcat.livingpictures.model.TopBarUiState
import com.github.bigfishcat.livingpictures.model.createBottomBarState
import com.github.bigfishcat.livingpictures.model.createTopBarUiState
import com.github.bigfishcat.livingpictures.ui.bar.BottomBar
import com.github.bigfishcat.livingpictures.ui.bar.TopBar
import com.github.bigfishcat.livingpictures.ui.popup.FigurePicker
import com.github.bigfishcat.livingpictures.ui.popup.PaletteColorPicker
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun LivingPicturesApp(modifier: Modifier = Modifier) {
    val appState = remember {
        mutableStateOf(AppUiState())
    }

    val topBarState = remember {
        mutableStateOf(TopBarUiState())
    }

    val bottomBarState = remember {
        mutableStateOf(BottomBarUiState())
    }

    val pagesRepository = remember {
        PagesRepository()
    }

    fun updateState(uiState: AppUiState) {
        appState.value = uiState
        bottomBarState.value = uiState.createBottomBarState()
        topBarState.value = uiState.createTopBarUiState()
    }

    fun handleAction(intent: Intent) =
        handleAction(intent, appState.value, pagesRepository, ::updateState)

    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(color = Background),
            topBar = { TopBar(topBarState, modifier, ::handleAction) },
            bottomBar = { BottomBar(bottomBarState, modifier, ::handleAction) }
        ) { innerPadding ->
            DrawingPage(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )

            when (appState.value.popupShown) {
                PopupShown.None -> {}
                PopupShown.PaletteColorPicker -> PaletteColorPicker(::handleAction)
                PopupShown.WheelColorPicker -> TODO()
                PopupShown.FiguresPicker -> FigurePicker(::handleAction)
                PopupShown.PagesPreview -> TODO()
            }
        }
    }
}

@Preview
@Composable
private fun DefaultLivingPicturesApp() {
    LivingPicturesTheme(darkTheme = true) {
        LivingPicturesApp()
    }
}