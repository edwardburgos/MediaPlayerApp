package com.example.mediaplayerapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mediaplayerapp.MyApplication

class DeleteReceiver : BroadcastReceiver() {

    //    private val playerManager = playerManager
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ddddddddddddddd", "Dffdfdffdfdf")
//        player
//    playerManager.
        Log.d("DALE PLAY", "HSGGHDSDGSHGD PAOLO")
//        context.applicationContext.servi
//       var cardi =  context.applicationContext.getSystemService(BackgroundServices::class.java)
//        println(cardi)
        val appp = context.applicationContext as MyApplication
        println("ahora hay notificaciones activas")
        println(appp.getPlayerManager().notificationManager.activeNotifications.size)
//        appp.getPlayerManager().idNoti.value = appp.getPlayerManager().idNoti.value + 1

//        var mainActivity: MainActivity? = null
//
//        fun setMainActivityHandler(main: MainActivity?){
//            mainActivity = main
//        }

//        playerManager.playStop()

//        cardi.cardi()
//        val i = Intent(context, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Foxandroid Alarm Manager")
//            .setContentText("Subscribe for android related content")
////            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
////            .la
////            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//        val notificationManagerCompat = NotificationManagerCompat.from(context)
//        notificationManagerCompat.notify(123, builder.build())
    }
}