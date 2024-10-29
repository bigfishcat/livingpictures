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
fun TopBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.undo_inactive),
                contentDescription = stringResource(id = R.string.undo)
            )
        }

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.redo_inactive),
                contentDescription = stringResource(id = R.string.redo)
            )
        }

        Spacer(modifier = modifier.weight(weight = 1.0f))

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.bin),
                contentDescription = stringResource(id = R.string.bin)
            )
        }

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.resource_new),
                contentDescription = stringResource(id = R.string.add_page)
            )
        }

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.layers),
                contentDescription = stringResource(id = R.string.all)
            )
        }

        Spacer(modifier = modifier.weight(weight = 1.0f))

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.pause_active),
                contentDescription = stringResource(id = R.string.play)
            )
        }

        IconButton(onClick = { /*TODO*/ }, modifier = modifier) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.play_inactive),
                contentDescription = stringResource(id = R.string.pause)
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun DefaultTopBar() {
    LivingPicturesTheme {
        TopBar()
    }
}