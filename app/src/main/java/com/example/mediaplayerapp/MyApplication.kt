package com.example.mediaplayerapp

import android.app.Application

class MyApplication: Application() {

    private var playerManager: PlayerManager? = null
    private var notificationId = 0

    fun setPlayerManager(playerManagerReceived: PlayerManager) {
        playerManager = playerManagerReceived
    }

    fun getPlayerManager(): PlayerManager? {
        return playerManager
    }

    fun getNotificationId(): Int {
        notificationId += 1
        return notificationId - 1
    }
}