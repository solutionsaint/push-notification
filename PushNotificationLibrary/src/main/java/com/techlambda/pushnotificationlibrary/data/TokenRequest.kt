package com.techlambda.pushnotificationlibrary.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("userId")
    @Expose
    val userId: String,
    @SerializedName("fcmToken")
    @Expose
    val fcmToken: String,
    @SerializedName("deviceId")
    @Expose
    val deviceId: String
)
