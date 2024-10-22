package com.techlambda.pushnotificationlibrary.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techlambda.pushnotificationlibrary.PushNotificationInitializer
import com.techlambda.pushnotificationlibrary.PushNotificationInitializer.customChannelConfig
import com.techlambda.pushnotificationlibrary.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM TOKEN", "onNewToken: $token")
        PushNotificationInitializer.token = token
//        sendNotificationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle FCM messages here.
        // For example, show a notification
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, message: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        PushNotificationInitializer.createNotificationChannel(this)

        // Intent to open the app when notification is tapped
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = customChannelConfig?.channelId ?: "default_channel"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Ensure you have this icon in your library
            .setContentTitle(title ?: "Notification")
            .setContentText(message ?: "")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}