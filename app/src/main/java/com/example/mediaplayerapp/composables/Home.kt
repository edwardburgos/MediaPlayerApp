package com.example.mediaplayerapp.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mediaplayerapp.PlayerManager
import com.example.mediaplayerapp.composables.SongsList

@Composable
fun Home(
    playerManager: PlayerManager,
    showDetails: (Int) -> Unit,
    setMediaPlayer: (Int) -> Unit,
    onAlarmIconClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Player App") },
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                actions = {
                    IconButton(onClick = { onAlarmIconClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = "Set alarm",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            )
        },
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
