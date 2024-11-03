package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.Black
import com.github.bigfishcat.livingpictures.ui.theme.Blue
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

    AlertDialog(
        onDismissRequest = dismiss,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.due),
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.delay_title),
                color = Black
            )
        },
        text = {
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    if (pattern.matches(it) && it.length < 5) text.value = it
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.delay_label),
                        color = Black
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { confirm() }
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = ::confirm,
                enabled = text.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = Blue
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = Blue
                )
            }
        }
    )
}

@Preview
@Composable
fun DefaultDuePickerDialog() {
    LivingPicturesTheme {
        DuePickerDialog(100, {}, {})
    }
}
