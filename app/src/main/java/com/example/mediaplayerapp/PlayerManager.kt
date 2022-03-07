package com.example.mediaplayerapp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import com.example.domain.Song
import java.io.BufferedReader
import java.io.InputStreamReader

class PlayerManager {

    var song: MediaPlayer? = null
    private var mmr: MediaMetadataRetriever? = null
    lateinit var cover: Bitmap
    var songName = "unknown"
    var artist = "unknown"
    var album = "unknown"
    var genero = "unknown"
    var currentSong: Int? = null
    private var contador = 0
    private lateinit var canciones: List<Int>
    var listenerWhenCompleted = false
    lateinit var genreFileLines: List<String>
    lateinit var songsBasicInformation: List<Song>

    fun createMediaPlayer(con: Context) {
        canciones = listOf(
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
        currentSong = canciones[contador]
        if (song == null) {
            val sharedPreferences = con?.getSharedPreferences("general_settings", MODE_PRIVATE)!!
            currentSong = sharedPreferences.getInt("currentSong", currentSong!!)
            contador = canciones.indexOf(currentSong)
            song = MediaPlayer.create(con!!, currentSong!!)
        }
    }

    fun nextSong(con: Context) {
        if (contador != 9) contador += 1 else contador = 0
        currentSong = canciones[contador]
        song?.stop()
        song?.reset()
        song?.release()
        song = MediaPlayer.create(con!!, currentSong!!)
        associateInfo(con!!)
        song?.start()
        listenerWhenCompleted = false
    }

    fun prevSong(con: Context) {
        if (contador != 0) contador -= 1 else contador = 9
        currentSong = canciones[contador]
        song?.stop()
        song?.reset()
        song?.release()
        song = MediaPlayer.create(con!!, currentSong!!)
        associateInfo(con!!)
        song?.start()
    }

    fun associateInfo(con: Context) {
        if (mmr == null) mmr = MediaMetadataRetriever()
        mmr?.let {
            it.setDataSource(
                con,
                Uri.parse("android.resource://com.example.mediaplayerapp/${currentSong!!}")
            )
            cover = createCover(it, con)
            songName = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            artist = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
            album = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
            genero = findGenreName(it)
        }
    }

    fun readGenres(con: Context) {
        genreFileLines =
            BufferedReader(InputStreamReader(con.resources.openRawResource(R.raw.genres))).readLines();
    }

    fun getSongsBasicDetails(con: Context) {
        if (mmr == null) mmr = MediaMetadataRetriever()
        songsBasicInformation = mmr?.let {
            canciones.map { cancion ->
                cancion
                it.setDataSource(
                    con,
                    Uri.parse("android.resource://com.example.mediaplayerapp/${cancion}")
                )
                Song(
                    cover = createCover(it, con),
                    name = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        .toString(),
                    artist = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                        .toString(),
                    album = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                        .toString(),
                    genero = findGenreName(it)
                )
            }
        } ?: listOf(
            Song(
                BitmapFactory.decodeResource(
                    con.resources,
                    R.drawable.ic_launcher_background
                ), "", "", "", ""
            )
        )
    }

    fun findGenreName(metadataRetriever: MediaMetadataRetriever): String {
        var genreCode =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE).toString()
                .substring(1, 3)
        var name = "unknown"
        for (element in genreFileLines) {
            if (element.substring(0, 2) == genreCode) {
                name = element.substring(3)
                break
            }
        }
        return name
    }

    fun saveCurrent(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences!!.edit()
        editor.putInt("current", song!!.currentPosition)
        editor.putInt("currentSong", currentSong!!)
        editor.commit()
    }

    fun finishMediaPlayer() {
        song?.stop()
        song?.reset()
        song?.release()
    }

    private fun createCover(mmr: MediaMetadataRetriever, con: Context): Bitmap {
        val embedPic = mmr.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic!!.size)
        return bitmap ?: BitmapFactory.decodeResource(
            con.resources,
            R.drawable.ic_launcher_background
        )
    }
}