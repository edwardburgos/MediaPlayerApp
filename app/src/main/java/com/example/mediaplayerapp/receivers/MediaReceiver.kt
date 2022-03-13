package com.example.mediaplayerapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mediaplayerapp.MyApplication
import com.example.mediaplayerapp.utils.NEXT
import com.example.mediaplayerapp.utils.PAUSE
import com.example.mediaplayerapp.utils.PLAY
import com.example.mediaplayerapp.utils.PREV

class MediaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as MyApplication
        when (intent.action) {
            PLAY, PAUSE -> application.getPlayerManager()?.playStop()
            NEXT -> application.getPlayerManager()?.nextSong()
            PREV -> application.getPlayerManager()?.prevSong()
        }
    }
}