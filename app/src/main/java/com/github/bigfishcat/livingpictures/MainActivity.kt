package com.github.bigfishcat.livingpictures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.github.bigfishcat.livingpictures.ui.LivingPicturesApp
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivingPicturesTheme {
                LivingPicturesApp(
                    context = this,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                )
            }
        }
    }
}

