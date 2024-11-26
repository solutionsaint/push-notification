package com.techlambda.pushnotificationlibrary

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Secure
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.techlambda.pushnotificationlibrary.data.TokenRequest
import com.techlambda.pushnotificationlibrary.di.PushNotificationEntryPoint
import com.techlambda.pushnotificationlibrary.repository.TokenRepository
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object PushNotificationInitializer {
    var customChannelConfig: ChannelConfig? = null
    var userId: String? = null
    var deviceId: String? = null
//    var token: String? = "e-3-L6bjQ7acS_-yPOq5u4:APA91bEdB2Hh7sQBY5YyoWnG1R1k-Ra4tPJnsgEJRLUiCkIc9umxMbSY9eaehlqtM5kRv9wqt5BrcCODu2-ssSFxYHrA6P7aVJqsJECvOgtXh6ZB1TkIGCifyqBQAPnpyq_s543HuzjD"
    var token: String? = null
    private lateinit var repository: TokenRepository


    data class ChannelConfig(
        val channelId: String,
        val channelName: String,
        val channelDescription: String,
        val importance: Int
    )

    fun initialize(context: Context, channelConfig: ChannelConfig? = null) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        customChannelConfig = channelConfig
        createNotificationChannel(context)
        /*deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)
        PushNotificationInitializer.userId = userId
        Log.d("Init", "initialize: DeviceId: $deviceId UserId: $userId")
        sendTokenToServer(context)*/
    }

    private fun sendTokenToServer(context: Context) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            PushNotificationEntryPoint::class.java
        )
        repository = entryPoint.pushNotificationRepository()

        if (token != null && userId != null && deviceId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.registerToken(
                    TokenRequest(
                        userId = userId!!,
                        fcmToken = token!!,
                        deviceId = deviceId!!
                    )
                )
            }
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val config = customChannelConfig ?: ChannelConfig(
                channelId = "default_channel_id",
                channelName = "Default Notifications",
                channelDescription = "Channel for default notifications",
                importance = NotificationManager.IMPORTANCE_HIGH
            )

            if (notificationManager.getNotificationChannel(config.channelId) == null) {
                val channel = NotificationChannel(
                    config.channelId,
                    config.channelName,
                    config.importance
                ).apply {
                    description = config.channelDescription
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun clearToken(): Task<Void> {
        return FirebaseMessaging.getInstance().deleteToken()
    }
    fun newToken(): Task<String> {
        return FirebaseMessaging.getInstance().token
    }
}