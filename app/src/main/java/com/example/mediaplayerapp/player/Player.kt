package com.example.mediaplayerapp.player

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.example.domain.Song
import com.example.mediaplayerapp.PlayerManager

@Composable
fun Player(
    playerManager: PlayerManager,
    song: Song,
    mediaPlayerPlayingState: Boolean,
    mediaPlayer: MediaPlayer,
    navController: NavHostController,
    showDetails: (Int) -> Unit,
    navigationAvailable: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (navigationAvailable) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Filled.ArrowBack,
                                contentDescription = "Go Back",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showDetails(song.id) }) {
                        Icon(
                            imageVector = Filled.MoreVert,
                            contentDescription = "See details",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = if (navigationAvailable) 16.dp else 0.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = song.cover,
                        builder = {
                            size(OriginalSize)
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = song.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = song.artist,
                maxLines = 1,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    mediaPlayer.let {
                        Icon(
                            imageVector = Filled.SkipPrevious,
                            contentDescription = "Previous song",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(if (navigationAvailable) 40.dp else 20.dp)
                                .clickable { playerManager.prevSong() }
                        )
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                imageVector = if (mediaPlayerPlayingState) Filled.Pause else Filled.PlayArrow,
                                contentDescription = "Play / Stop",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .size(if (navigationAvailable) 70.dp else 40.dp)
                                    .clickable { playerManager.playStop() }
                            )
                        }
                        Icon(
                            imageVector = Filled.SkipNext,
                            contentDescription = "Next song",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(if (navigationAvailable) 40.dp else 20.dp)
                                .clickable { playerManager.nextSong() }
                        )
                    }
                }
            }
        }
    }
}