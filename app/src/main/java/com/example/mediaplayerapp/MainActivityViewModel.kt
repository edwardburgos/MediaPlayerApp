package com.example.mediaplayerapp

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val mediaPlayer = PlayerManager()
    var mediaPlayerCreated = false
}