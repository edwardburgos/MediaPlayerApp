package com.example.mediaplayerapp

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder

class BackgroundServices : Service() {

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        val application = applicationContext as MyApplication
        application.getPlayerManager()?.saveCurrent()
        application.getPlayerManager()?.mediaPlayer?.value?.let {
            it.stop()
            it.reset()
            it.release()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}