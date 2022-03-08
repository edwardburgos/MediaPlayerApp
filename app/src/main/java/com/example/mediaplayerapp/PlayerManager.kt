package com.example.mediaplayerapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.domain.Song

class PlayerManager {

    val song = mutableStateOf(Song(
        -1,
        Bitmap.createBitmap(15, 26, Bitmap.Config.ARGB_8888),
        "unknown",
        "unknown",
        "unknown",
        "unknown"
    ))

    val mediaPlayer = mutableStateOf<MediaPlayer?>(null)
    val mediaPlayerPlayingState = mutableStateOf(false)
//    var mediaPlayer: MediaPlayer? = null
//    var song =

    //    lateinit var cover: Bitmap
//    var songName = "unknown"
//    var artist = "unknown"
//    var album = "unknown"
//    var genero = "unknown"
    var currentSong = -1 // ESTA ES EL INT DEL RESOURCE

    //    private var contador = 0
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
    var listenerWhenCompleted = false
    lateinit var songsBasicInformation: List<Song>
    private lateinit var appContext: Context

    fun createMediaPlayer(con: Context, songId: Int, requestedByUser: Boolean) {
        appContext = con
//        currentSong = canciones[contador]
//        if (song.id == -1) {
        var songResourceIntId = -1
        if (songId == -1) {
            songResourceIntId = con?.getSharedPreferences("general_settings", MODE_PRIVATE).getInt("currentSong", -1)
//            songId =
        //            var sharedValue = sharedPreferences.getInt("currentSong", -1)
        } else {
            songResourceIntId = canciones.elementAt(songId)
        }

        if (songResourceIntId != -1) {
            currentSong = songResourceIntId
            associateInfo()
            if (mediaPlayer != null) {
                mediaPlayer.value?.stop()
                mediaPlayer.value?.reset()
                mediaPlayer.value?.release()
            }
            song.value.id = canciones.indexOf(currentSong)
            mediaPlayer.value = MediaPlayer.create(con!!, currentSong!!)
            if (requestedByUser) {
                mediaPlayer.value?.start()
                mediaPlayerPlayingState.value = true
            }
        }
//        }
    }

    fun nextSong() {
        if (song.value.id != canciones.size - 1) song.value.id += 1 else song.value.id = 0
        currentSong = canciones[song.value.id]
        mediaPlayer.value?.stop()
        mediaPlayer.value?.reset()
        mediaPlayer.value?.release()
        mediaPlayer.value = MediaPlayer.create(appContext, currentSong)
        associateInfo()
        mediaPlayer.value?.start()
        mediaPlayerPlayingState.value = true
        listenerWhenCompleted = false
    }

    fun prevSong() {
        if (song.value.id != 0) song.value.id -= 1 else song.value.id = canciones.size - 1
        currentSong = canciones[song.value.id]
        mediaPlayer.value?.stop()
        mediaPlayer.value?.reset()
        mediaPlayer.value?.release()
        mediaPlayer.value = MediaPlayer.create(appContext, currentSong)
        associateInfo()
        mediaPlayer.value?.start()
        mediaPlayerPlayingState.value = true
    }

    fun associateInfo() {
        var metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(
            appContext,
            Uri.parse("android.resource://com.example.mediaplayerapp/${currentSong!!}")
        )
        song.value.cover = createCover(metadataRetriever, appContext)
        song.value.name =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
        song.value.artist =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
        song.value.album =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
        song.value.genre =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE).toString()

    }

    fun getSongsBasicDetails(con: Context) {
        var metadataRetriever = MediaMetadataRetriever()
        songsBasicInformation =
            canciones.mapIndexed { index, cancion ->
                cancion
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

    fun saveCurrent(sharedPreferences: SharedPreferences) {
//        sharedPreferences = con?.getSharedPreferences("general_settings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("current", mediaPlayer.value?.let { it.currentPosition } ?: 0)
        editor.putInt("currentSong", currentSong)
        editor.commit()
    }

    fun finishMediaPlayer() {
        mediaPlayer.value?.stop()
        mediaPlayer.value?.reset()
        mediaPlayer.value?.release()
    }

    private fun createCover(mmr: MediaMetadataRetriever, con: Context): Bitmap {
        val embedPic = mmr.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic!!.size)
        return bitmap ?: BitmapFactory.decodeResource(
            con.resources,
            R.drawable.ic_baseline_music_note_24
        )
    }

    fun playStop() {
        val sharedPreferences = appContext?.getSharedPreferences("general_settings", MODE_PRIVATE)
        mediaPlayer.value?.let {
            if (it.isPlaying) {

                it.pause()
                mediaPlayerPlayingState.value = false
                saveCurrent(sharedPreferences)
//                binding.playStopButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
//                paused = true
//                (activity as MainActivity).manager.saveCurrent(sharedPreferences)
            } else {
//                val current = sharedPreferences.getInt("current", 0)
                it.seekTo(sharedPreferences.getInt("current", 0))
                it.start()
                mediaPlayerPlayingState.value = true

            }
        }

//        if (!(activity as MainActivity).manager.song!!.isPlaying()) {
//            binding.playStopButton.setImageResource(R.drawable.ic_baseline_pause_24)
//            val current = sharedPreferences.getInt("current", 0)
//            (activity as MainActivity).manager.song!!.seekTo(current)
//            (activity as MainActivity).manager.song!!.start()
//        } else {
//            (activity as MainActivity).manager.song!!.pause()
//            binding.playStopButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
//            paused = true
//            (activity as MainActivity).manager.saveCurrent(sharedPreferences)
//        }
    }
}