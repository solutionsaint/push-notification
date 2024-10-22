package com.techlambda.pushnotificationlibrary.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("_id")
    @Expose
    val _id: String,
    @SerializedName("userId")
    @Expose
    val userId: String,
    @SerializedName("deviceId")
    @Expose
    val deviceId: String,
    @SerializedName("fcmToken")
    @Expose
    val fcmToken: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String,
    @SerializedName("__v")
    @Expose
    val __v: Int
)