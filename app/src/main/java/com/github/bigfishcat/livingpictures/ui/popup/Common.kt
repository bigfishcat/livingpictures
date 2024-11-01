package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.PopupStroke

@Composable
fun PopupContentContainer(content: @Composable RowScope.() -> Unit) {
    Card(
        modifier = Modifier
            .background(brush = SolidColor(Background), alpha = 0.16f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, PopupStroke)
    ) {
        Row(modifier = Modifier.padding(16.dp), content = content)
    }
}