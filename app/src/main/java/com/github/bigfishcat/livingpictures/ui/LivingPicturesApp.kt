package com.github.bigfishcat.livingpictures.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.ui.bar.BottomBar
import com.github.bigfishcat.livingpictures.ui.bar.TopBar
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun LivingPicturesApp(modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            modifier = modifier.fillMaxSize().background(color = Background),
            topBar = { TopBar(modifier) },
            bottomBar = { BottomBar(modifier) }
        ) { innerPadding ->
            DrawingPage(modifier = modifier.padding(innerPadding).fillMaxSize())
        }
    }
}

@Preview
@Composable
fun DefaultLivingPicturesApp() {
    LivingPicturesTheme(darkTheme = true) {
        LivingPicturesApp()
    }
}