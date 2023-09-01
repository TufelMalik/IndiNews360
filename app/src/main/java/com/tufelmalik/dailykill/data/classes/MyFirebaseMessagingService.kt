package com.tufelmalik.dailykill.data.classes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.ui.activity.MainActivity
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val customValue = remoteMessage.data["custom_key"]

        if (title != null && body != null) {
            showNotification(title, body, customValue)
        }
    }

    private fun showNotification(title: String, body: String, customValue: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, "default")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Include custom data in the notification
        if (customValue != null) {
            notificationBuilder.setContentText("$body - Custom Data: $customValue")
        }

        // Generate a random notification ID
        val notificationId = Random.nextInt()

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
