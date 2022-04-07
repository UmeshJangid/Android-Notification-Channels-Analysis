package com.ns_poc.fcmpushnotification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException
import com.google.firebase.firestore.CollectionReference

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


fun Toast.showToast() {

}

class MainActivity : AppCompatActivity() {

    private val senderID = "YOUR_SENDER_ID"
    private val TAG = "MainActivity"
    private val db: FirebaseFirestore? = null
    private val colRefUsers: CollectionReference? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getToken()
        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        button1.setOnClickListener {
            MyFirebaseMessagingService.setupNotificationChannels(
                MyFirebaseMessagingService.channelId,
                MyFirebaseMessagingService.channelName,
                notificationManager
            )
            showToast("Notification Channel Created.")
        }

        button2.setOnClickListener {
            MyFirebaseMessagingService.updateNotificationChannel(
                MyFirebaseMessagingService.channelId,
                "MyUpdatedChannel",
                "This is my updated description",
                false,
                3,
                notificationManager
            )
            showToast("Notification Channel updated.")
        }

        button3.setOnClickListener {
            MyFirebaseMessagingService.deleteNotificationChannel(
                MyFirebaseMessagingService.channelId,
                notificationManager
            )
            showToast("Notification Channel deleted.")
        }

        button4.setOnClickListener {
            MyFirebaseMessagingService.setupNotificationChannels(
                "MyDifferentChannelID", "Different Name",
                notificationManager
            )
            showToast("Different Notification Channel created.")
        }

        button5.setOnClickListener {
            MyFirebaseMessagingService.updateDeletedNotificationChannels(
                MyFirebaseMessagingService.channelId,
                "Updated Channel which was previously deleted",
                "Notification Channel updated with same channel id which was deleted before.",
                notificationManager
            )
            showToast("Notification Channel updated with same channel id which was deleted before.")
        }


    }


    fun Any.showToast(string: String) {
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()
    }

    private fun getToken() {

        Thread(Runnable {
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    // Get new FCM registration token
                    val token = task.result
                    // Log and toast
                    if (!token.isNullOrEmpty()) {
                        Log.d(TAG, token!!)
                        Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                    }
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

}


