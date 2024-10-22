package com.techlambda.pushnotificationlibrary.di

import com.techlambda.pushnotificationlibrary.repository.TokenRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PushNotificationEntryPoint {
    fun pushNotificationRepository(): TokenRepository
}