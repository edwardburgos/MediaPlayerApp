package com.example.mediaplayerapp.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.domain.Song

@Composable
fun SongsList(
    songs: List<Song>,
    showDetails: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(songs.size) { index ->
            SongItem(
                songs.elementAt(index),
                index,
                showDetails
            )
        }
    }
}