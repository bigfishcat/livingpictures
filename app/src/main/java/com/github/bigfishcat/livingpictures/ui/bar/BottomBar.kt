package com.github.bigfishcat.livingpictures.ui.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.pencil),
                contentDescription = stringResource(id = R.string.pencil)
            )
        }
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.brush),
                contentDescription = stringResource(id = R.string.brush)
            )
        }
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.erase),
                contentDescription = stringResource(id = R.string.eraser)
            )
        }
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.instruments),
                contentDescription = stringResource(id = R.string.instruments)
            )
        }
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.circle),
                contentDescription = stringResource(id = R.string.color)
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun DefaultBottomBar() {
    LivingPicturesTheme {
        BottomBar()
    }
}
