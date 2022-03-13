package com.example.mediaplayerapp

import android.app.*
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.*
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import com.example.mediaplayerapp.receivers.AlarmReceiver
import com.example.mediaplayerapp.ui.theme.MediaPlayerAppTheme
import com.example.mediaplayerapp.utils.*
import java.util.*
import androidx.media.app.NotificationCompat as MediaNotificationCompat

class MainActivity : ComponentActivity() {

    private lateinit var notificationManager: NotificationManager

    // Alarm Notification Channel
    private val alarmChannel = NotificationChannel(
        ALARM_CHANNEL_ID,
        ALARM_CHANNEL_NAME,
        IMPORTANCE_HIGH
    ).apply {
        description = ALARM_CHANNEL_DESCRIPTION
    }

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivityViewModel: MainActivityViewModel by viewModels()

        startService(Intent(this, BackgroundServices::class.java))

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Alarm Notification Channel
        notificationManager.deleteNotificationChannel(ALARM_CHANNEL_ID)
        notificationManager.createNotificationChannel(alarmChannel)

        // Media Notification Channel
        val mediaChannel = NotificationChannel(
            MEDIA_CHANNEL_ID,
            MEDIA_CHANNEL_NAME,
            IMPORTANCE_HIGH
        ).apply {
            description = MEDIA_CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(mediaChannel)

        val mediaSession = MediaSessionCompat(this, "PlayerService")
        val notification = NotificationCompat.Builder(this, MEDIA_CHANNEL_ID)
            .setStyle(
                MediaNotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)

        // AudioManager
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val audioFocusChangeListener = OnAudioFocusChangeListener {
            fun onAudioFocusChange(focusChange: Int) {
                when (focusChange) {
                    AUDIOFOCUS_LOSS_TRANSIENT -> println("ALGO 1")
                    AUDIOFOCUS_GAIN -> println("ALGO 2")
                    AUDIOFOCUS_LOSS -> println("ALGO 3")
                }
            }
        }

        val focusRequest = AudioFocusRequest.Builder(AUDIOFOCUS_GAIN).run {
            setAudioAttributes(AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_GAME)
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                build()
            })
            setAcceptsDelayedFocusGain(true)
            setOnAudioFocusChangeListener(audioFocusChangeListener)
            build()
        }

        // PlayerManager
        if (!mainActivityViewModel.mediaPlayerCreated) {
            mainActivityViewModel.mediaPlayer.setVariablesANDCreateMediaPlayer(
                applicationContext,
                notificationManager,
                notification,
                audioManager,
                focusRequest,
                -1,
                false
            )
        }

        val myApplication = applicationContext as MyApplication
        myApplication.setPlayerManager(mainActivityViewModel.mediaPlayer)

        mainActivityViewModel.mediaPlayerCreated = true

        fun setAlarm(hour: Int, minutes: Int, amPm: AmPm) {
            val intent = Intent(applicationContext, AlarmReceiver::class.java)
            val alarmManager =
                applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, if (amPm == AmPm.PM) hour + 12 else hour)
            selectedTime.set(Calendar.MINUTE, minutes)
            selectedTime.set(Calendar.SECOND, 0)
            selectedTime.set(Calendar.MILLISECOND, 0)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    this, selectedTime.timeInMillis.toInt(), intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                selectedTime.timeInMillis,
                pendingIntent
            )
            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
        }

        setContent {
            MediaPlayerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppComposable(
                        mainActivityViewModel.mediaPlayer
                    ) { hour, minute, amPm -> setAlarm(hour, minute, amPm) }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationManager.deleteNotificationChannel(ALARM_CHANNEL_ID)
        notificationManager.createNotificationChannel(alarmChannel)
    }
}
