package com.techlambda.pushnotificationlibrary.service

import com.techlambda.pushnotificationlibrary.data.TokenRequest
import com.techlambda.pushnotificationlibrary.data.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("notifications/register")
    suspend fun registerToken(@Body tokenRequest: TokenRequest): Response<TokenResponse>
}