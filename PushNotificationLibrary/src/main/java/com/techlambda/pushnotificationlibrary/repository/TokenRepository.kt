package com.techlambda.pushnotificationlibrary.repository

import android.widget.Toast
import com.techlambda.pushnotificationlibrary.data.TokenRequest
import com.techlambda.pushnotificationlibrary.data.TokenResponse
import com.techlambda.pushnotificationlibrary.service.ApiService
import javax.inject.Inject

class TokenRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun registerToken(tokenRequest: TokenRequest): Result<TokenResponse> {
        return try {
            val response = apiService.registerToken(tokenRequest = tokenRequest)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Unknown error"))
                }
            } else {
                Result.failure(Exception("Network call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}