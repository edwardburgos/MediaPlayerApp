package com.example.domain

import android.graphics.Bitmap

data class Song(
    var cover: Bitmap,
    var name: String,
    var artist: String,
    var album: String,
    val genero: String
)