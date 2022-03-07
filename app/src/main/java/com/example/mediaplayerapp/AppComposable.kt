package com.example.mediaplayerapp

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mediaplayerapp.home.Home
import com.example.mediaplayerapp.home.HomeViewModel

@Composable
fun AppComposable(playerManager: PlayerManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Home(playerManager, navController, HomeViewModel())
        }
    }
}