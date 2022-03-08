package com.example.mediaplayerapp

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerapp.details.Details
import com.example.mediaplayerapp.home.Home
import kotlinx.coroutines.launch
import androidx.compose.material.rememberModalBottomSheetState

@ExperimentalMaterialApi
@Composable
fun AppComposable(playerManager: PlayerManager) {
    val navController = rememberNavController()
    val songId = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Details(playerManager.songsBasicInformation.elementAt(songId.value))
        }
    ) {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(
                    playerManager,
                ) { id ->
                    songId.value = id
                    scope.launch { state.show() }
                }
            }
        }
    }
}