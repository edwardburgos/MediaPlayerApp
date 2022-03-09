package com.example.mediaplayerapp.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mediaplayerapp.PlayerManager
import com.example.mediaplayerapp.composables.SongsList

@Composable
fun Home(
    playerManager: PlayerManager,
    showDetails: (Int) -> Unit,
    setMediaPlayer: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Player App") },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) {
        Column {
            SongsList(
                playerManager.songsBasicInformation,
                showDetails,
                setMediaPlayer
            )
        }
    }
}
