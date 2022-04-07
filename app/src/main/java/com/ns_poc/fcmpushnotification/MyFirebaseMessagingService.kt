package com.ns_poc.fcmpushnotification

import android.app.Notification.BADGE_ICON_NONE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val tag = "FirebaseMessagingService"
    private val TAG = "MyFirebaseMessagingServ"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("$tag token --> $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (remoteMessage.notification != null) {
                Log.d(TAG, "onMessageReceived: if Block ")
                showNotification(
                    remoteMessage.notification?.title,
                    remoteMessage.notification?.body
                )
            } else {
                Log.d(TAG, "onMessageReceived: Else Block ")
                showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
            }

        } catch (e: Exception) {
            println("$tag error -->${e.localizedMessage}")
        }
    }

    private fun showNotification(
        title: String?,
        body: String?
    ) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels(channelId, channelName, notificationManager)
        }

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        val channelId = "mydefaultchannelid"
        val channelName = "mydefaultchannel"
        @JvmStatic
        @RequiresApi(Build.VERSION_CODES.O)
        public fun setupNotificationChannels(
            channelId: String,
            channelName: String,
            notificationManager: NotificationManager
        ) {
            try {
                val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.setShowBadge(true)
                channel.description = channelName
                notificationManager.createNotificationChannel(channel)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        @JvmStatic
        @RequiresApi(Build.VERSION_CODES.O)
        public fun updateDeletedNotificationChannels(
            channelId: String,
            channelName: String,
            channelDes:String,
            notificationManager: NotificationManager
        ) {
            try {
                val channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
                channel.enableLights(false)
                channel.lightColor = Color.GREEN
                channel.enableVibration(false)
                channel.setShowBadge(false)
                channel.description = channelDes
                notificationManager.createNotificationChannel(channel)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        @RequiresApi(api = Build.VERSION_CODES.O)
        public fun deleteNotificationChannel(
            channelId: String,
            notificationManager: NotificationManager
        ) {
            try {
                notificationManager.deleteNotificationChannel(channelId)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        @JvmStatic
        @RequiresApi(api = Build.VERSION_CODES.O)
        public fun updateNotificationChannel(
            channelId: String,
            channelName: String,
            channelDes: String,
            showBadge:Boolean,
            importance:Int,
            notificationManager: NotificationManager
        ) {
            try {
                val channel =
                    NotificationChannel(channelId, channelName, importance)
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.setShowBadge(showBadge)
                channel.description = channelDes
                notificationManager.createNotificationChannel(channel)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private fun setupNotificationChannels(
//        channelId: String,
//        channelName: String,
//        notificationManager: NotificationManager
//    ) {
//
//        val channel =
//            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
//        channel.enableLights(true)
//        channel.lightColor = Color.RED
//        channel.enableVibration(true)
//        channel.setShowBadge(false)
//        notificationManager.createNotificationChannel(channel)
//    }


}