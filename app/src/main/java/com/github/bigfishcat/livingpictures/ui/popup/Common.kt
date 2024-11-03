package com.github.bigfishcat.livingpictures.ui.popup

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.DialogBackground
import com.github.bigfishcat.livingpictures.ui.theme.PopupBackground
import com.github.bigfishcat.livingpictures.ui.theme.PopupStroke
import com.github.bigfishcat.livingpictures.ui.theme.Selected

@Composable
fun PopupContentContainer(content: @Composable RowScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, PopupStroke),
        colors = CardColors(
            containerColor = PopupBackground,
            contentColor = PopupBackground,
            disabledContainerColor = PopupBackground,
            disabledContentColor = PopupBackground
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun ConfirmDialog(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    confirm: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        containerColor = DialogBackground,
        onDismissRequest = dismiss,
        icon = {
            Icon(painter = painterResource(id = iconRes), contentDescription = null)
        },
        title = {
            Text(
                text = stringResource(id = titleRes)
            )
        },
        confirmButton = {
            TextButton(onClick = confirm) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = Selected
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = Selected
                )
            }
        }
    )
}

@Composable
fun TextFieldDialog(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    @StringRes labelRes: Int,
    text: State<String>,
    onValueChange: (String) -> Unit,
    confirm: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        containerColor = DialogBackground,
        onDismissRequest = dismiss,
        icon = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(id = titleRes)
            )
        },
        text = {
            OutlinedTextField(
                value = text.value,
                onValueChange = onValueChange,
                label = {
                    Text(
                        text = stringResource(id = labelRes)
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
                onClick = confirm,
                enabled = text.value.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    color = Selected
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = Selected
                )
            }
        }
    )
}