package com.example.mediaplayerapp

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerapp.details.Details
import com.example.mediaplayerapp.home.Home
import kotlinx.coroutines.launch
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.mediaplayerapp.player.Player

@ExperimentalMaterialApi
@Composable
fun AppComposable(playerManager: PlayerManager, context: Context) {
    val navController = rememberNavController()
    val songId = remember { mutableStateOf(0) }
    val configuration = LocalConfiguration.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            Details(
                scrollState,
                playerManager.songsBasicInformation.elementAt(songId.value),
                state,
                { scope.launch { state.hide() } },
                configuration
            )
        }
    ) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(playerManager.mediaPlayer.value?.let { 0.8f } ?: 1f)
                ) {
                    Home(
                        playerManager,
                        { id ->
                            songId.value = id
                            scope.launch {
                                scrollState.scrollTo(0)
                                state.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        }) { id ->
                        playerManager.createMediaPlayer(context, id, true)
                    }
                }
                playerManager.mediaPlayer.value?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .background(Color.Red)
                    ) {
                        Player(
                            playerManager,
                            playerManager.song.value,
                            playerManager.mediaPlayerPlayingState.value,
                            it,
                            navController,
                            { id ->
                                songId.value = id
                                scope.launch {
                                    scrollState.scrollTo(0)
                                    state.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            },
                            false
                        )
                    }

                }
            }
        } else {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    Home(
                        playerManager,
                        { id ->
                            songId.value = id
                            scope.launch {
                                scrollState.scrollTo(0)
                                state.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        }) { id ->
                        navController.navigate("player")
                        playerManager.createMediaPlayer(context, id, true)
                    }
                }
                composable("player") {
                    playerManager.mediaPlayer.value?.let {
                        Player(
                            playerManager,
                            playerManager.song.value,
                            playerManager.mediaPlayerPlayingState.value,
                            it,
                            navController,
                            { id ->
                                songId.value = id
                                scope.launch {
                                    scrollState.scrollTo(0)
                                    state.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            },
                            true
                        )
                    }
                }
            }
        }
    }
}