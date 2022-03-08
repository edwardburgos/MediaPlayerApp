package com.example.mediaplayerapp.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.domain.Song

@Composable
fun Details(song: Song) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = song.cover,
                        builder = {
                            size(OriginalSize)
                        }
                    ),
                    contentDescription = null,

                    modifier = Modifier
                        .height(140.dp)
                )
                Text(
                    text = song.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(text = song.artist, color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
        Text(
            text = "Interpreted by",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 20.dp)
        )
        val artists = song.artist.split(", ")
        artists.forEachIndexed { index, artist ->
            Text(
                text = artist,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = if (index == 0) 4.dp else 2.dp,
                    bottom = if (index != artists.size - 1) 0.dp else 16.dp
                )
            )
        }
        Text(
            text = "Album",
            style = MaterialTheme.typography.h6
        )
        Text(song.album, modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 4.dp))
        Text(text = "Genre", style = MaterialTheme.typography.h6)
        Text(song.genre, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
    }
}