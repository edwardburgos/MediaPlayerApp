package com.example.mediaplayerapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.*
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.domain.Song
import com.example.mediaplayerapp.receivers.MediaReceiver
import com.example.mediaplayerapp.utils.*

class PlayerManager {

    val song = mutableStateOf(
        Song(
            -1,
            Bitmap.createBitmap(15, 26, Bitmap.Config.ARGB_8888),
            "unknown",
            "unknown",
            "unknown",
            "unknown"
        )
    )
    private val notificationId = mutableStateOf(0)
    val mediaPlayer = mutableStateOf<MediaPlayer?>(null)
    val mediaPlayerPlayingState = mutableStateOf(false)
    private var currentSong = -1
    private val canciones: List<Int> = listOf(
        R.raw.tainylosientobb,
        R.raw.badgyalrema44,
        R.raw.debilidad,
        R.raw.entrenosotrosremix,
        R.raw.harakakikolosbobosonmio,
        R.raw.inmortales,
        R.raw.remix911,
        R.raw.tiagobiza,
        R.raw.truenodancecrip,
        R.raw.verteir,
        R.raw.tainylosientobb,
        R.raw.badgyalrema44,
        R.raw.debilidad,
        R.raw.entrenosotrosremix,
        R.raw.harakakikolosbobosonmio,
        R.raw.inmortales,
        R.raw.remix911,
        R.raw.tiagobiza,
        R.raw.truenodancecrip,
        R.raw.verteir,
        R.raw.tainylosientobb,
        R.raw.badgyalrema44,
        R.raw.debilidad,
        R.raw.entrenosotrosremix,
        R.raw.harakakikolosbobosonmio,
        R.raw.inmortales,
        R.raw.remix911,
        R.raw.tiagobiza,
        R.raw.truenodancecrip,
        R.raw.verteir
    )
    private var listenerWhenCompleted = false
    lateinit var songsBasicInformation: List<Song>
    private lateinit var appContext: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var audioManager: AudioManager
    private lateinit var focusRequest: AudioFocusRequest
    private lateinit var sharedPreferences: SharedPreferences

    fun setVariablesANDCreateMediaPlayer(
        con: Context,
        notificationManagerReceived: NotificationManager,
        notificationBuilderReceived: NotificationCompat.Builder,
        audioManagerReceived: AudioManager,
        focusRequestReceived: AudioFocusRequest,
        songId: Int,
        requestedByUser: Boolean,
    ) {
        appContext = con
        notificationManager = notificationManagerReceived
        notificationBuilder = notificationBuilderReceived
        audioManager = audioManagerReceived
        focusRequest = focusRequestReceived
        sharedPreferences = appContext.getSharedPreferences("general_settings", MODE_PRIVATE)
        getSongsBasicDetails(con)
        createMediaPlayer(songId, requestedByUser)
    }

    fun createMediaPlayer(songId: Int, requestedByUser: Boolean) {
        val songResourceIntId = if (songId == -1) {
            sharedPreferences.getInt("currentSong", -1)
        } else {
            canciones.elementAt(songId)
        }
        if (songResourceIntId != -1) {
            currentSong = songResourceIntId
            associateInfo()
            mediaPlayer.value?.stop()
            mediaPlayer.value?.reset()
            mediaPlayer.value?.release()
            song.value.id = canciones.indexOf(currentSong)
            mediaPlayer.value = MediaPlayer.create(appContext, currentSong)
            mediaPlayer.value?.setOnCompletionListener {
                nextSong()
            }
            if (requestedByUser) {
                audioManager.requestAudioFocus(focusRequest)
                mediaPlayer.value?.start()
                mediaPlayerPlayingState.value = true
                updateNotification()
            }
        }
    }

    fun nextSong() {
        if (song.value.id != canciones.size - 1) song.value.id += 1 else song.value.id = 0
        currentSong = canciones[song.value.id]
        mediaPlayer.value?.stop()
        mediaPlayer.value?.reset()
        mediaPlayer.value?.release()
        mediaPlayer.value = MediaPlayer.create(appContext, currentSong)
        mediaPlayer.value?.setOnCompletionListener {
            nextSong()
        }
        associateInfo()
        mediaPlayer.value?.start()
        audioManager.requestAudioFocus(focusRequest)
        mediaPlayerPlayingState.value = true
        updateNotification()
        listenerWhenCompleted = false
    }

    fun prevSong() {
        if (song.value.id != 0) song.value.id -= 1 else song.value.id = canciones.size - 1
        currentSong = canciones[song.value.id]
        mediaPlayer.value?.stop()
        mediaPlayer.value?.reset()
        mediaPlayer.value?.release()
        mediaPlayer.value = MediaPlayer.create(appContext, currentSong)
        mediaPlayer.value?.setOnCompletionListener {
            nextSong()
        }
        associateInfo()
        mediaPlayer.value?.start()
        audioManager.requestAudioFocus(focusRequest)
        mediaPlayerPlayingState.value = true
        updateNotification()
    }

    private fun associateInfo() {
        val songInfo = songsBasicInformation[canciones.indexOf(currentSong)]
        song.value.cover = songInfo.cover
        song.value.name = songInfo.name
        song.value.artist = songInfo.artist
        song.value.album = songInfo.album
        song.value.genre = songInfo.genre
    }

    private fun getSongsBasicDetails(con: Context) {
        val metadataRetriever = MediaMetadataRetriever()
        songsBasicInformation =
            canciones.mapIndexed { index, cancion ->
                metadataRetriever.setDataSource(
                    con,
                    Uri.parse("android.resource://com.example.mediaplayerapp/${cancion}")
                )
                Song(
                    index,
                    createCover(metadataRetriever, con),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString(),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                        .toString(),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                        .toString(),
                    metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                        .toString()
                )
            }
    }

    fun saveCurrent() {
        val editor = sharedPreferences.edit()
        editor.putInt("current", mediaPlayer.value?.currentPosition ?: 0)
        editor.putInt("currentSong", currentSong)
        editor.apply()
    }

    private fun createCover(mmr: MediaMetadataRetriever, con: Context): Bitmap {
        return mmr.embeddedPicture?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        } ?: BitmapFactory.decodeResource(
            con.resources,
            R.drawable.ic_baseline_music_note_24
        )
    }

    fun playStop() {
        mediaPlayer.value?.let {
            if (it.isPlaying) {
                it.pause()
                mediaPlayerPlayingState.value = false
                saveCurrent()
                updateNotification()
            } else {
                it.seekTo(sharedPreferences.getInt("current", 0))
                it.start()
                audioManager.requestAudioFocus(focusRequest)
                mediaPlayerPlayingState.value = true
                updateNotification()
            }
        }
    }

    private fun updateNotification() {
        mediaPlayer.value?.let {
            with(NotificationManagerCompat.from(appContext)) {
                val finalNotification = notificationBuilder
                finalNotification
                    .setSilent(true)
                    .setLargeIcon(song.value.cover)
                    .setContentTitle(song.value.name)
                    .setContentText(song.value.artist)
                val actions = listOf(PREV, if (it.isPlaying) PLAY else PAUSE, NEXT)
                finalNotification.clearActions()
                actions.forEach {
                    val intent = Intent(appContext, MediaReceiver::class.java)
                    intent.action = it
                    val icon = when (it) {
                        PREV -> R.drawable.ic_baseline_skip_previous_24
                        PLAY -> R.drawable.ic_baseline_pause_24
                        PAUSE -> R.drawable.ic_baseline_play_arrow_24
                        else -> R.drawable.ic_baseline_skip_next_24
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        appContext,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    finalNotification.addAction(
                        NotificationCompat.Action.Builder(icon, it, pendingIntent).build()
                    )
                }
                notify(notificationId.value, finalNotification.build())
            }
        }
    }
}