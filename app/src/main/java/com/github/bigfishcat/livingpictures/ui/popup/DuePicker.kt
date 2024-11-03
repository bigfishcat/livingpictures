package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun PlayDuePickerDialog(appState: AppUiState, action: (Intent) -> Unit = {}) {
    DuePickerDialog(
        appState.playbackDelay,
        { action.invoke(Intent.Play(it)) },
        { action.invoke(Intent.HidePopup) }
    )
}

@Composable
fun DuePickerDialog(initialValue: Long, confirm: (Long) -> Unit, dismiss: () -> Unit) {
    val text = remember { mutableStateOf(initialValue.toString()) }
    val pattern = remember { Regex("^\\d+\$") }

    fun confirm() {
        confirm(text.value.toLongOrNull() ?: 1000L)
    }

    TextFieldDialog(
        iconRes = R.drawable.due,
        titleRes = R.string.delay_title,
        labelRes = R.string.delay_label,
        text = text,
        onValueChange = { if (pattern.matches(it) && it.length < 5) text.value = it },
        confirm = ::confirm,
        dismiss = dismiss
    )
}

@Preview
@Composable
fun DefaultDuePickerDialog() {
    LivingPicturesTheme {
        DuePickerDialog(100, {}, {})
    }
}
