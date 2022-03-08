package com.example.mediaplayerapp.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.mediaplayerapp.PlayerManager
import com.example.mediaplayerapp.composables.SongsList

@Composable
fun Home(
    playerManager: PlayerManager,
    showDetails: (Int) -> Unit,
    setMediaPlayer: (Int) -> Unit
) {
    Column {
        SongsList(
            playerManager.songsBasicInformation,
            showDetails,
            setMediaPlayer
        )
    }
}
