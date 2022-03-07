package com.example.mediaplayerapp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.domain.Song

@Composable
fun SongItem(
    song: Song,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = if (index == 0) 16.dp else 0.dp,
                end = 16.dp,
                bottom = 16.dp
            )
            .pointerInput(Unit) {
            },
        elevation = 4.dp
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                )
                {
                    Image(
                        painter = rememberImagePainter(
                            data = song.cover,
                            builder = {
                                size(OriginalSize)
                            }
                        ),
                        contentDescription = null,

                        modifier = Modifier
                            .height(70.dp)
                            .padding(end = 16.dp),
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Column {
                            Text(
                                text = song.name,
                                style = MaterialTheme.typography.body1,
                                color = Color.Black,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(
                                text = song.artist,
                                style = MaterialTheme.typography.body2,
                                color = Color.Gray,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "See details",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}