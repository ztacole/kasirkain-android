package com.takumi.kasirkain.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val tokenProvider: suspend () -> String?,
    private val clearToken: suspend () -> Unit
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = runBlocking { tokenProvider() }

        return if (token.isNullOrEmpty()) {
            chain.proceed(originalRequest)
        } else {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()
            val response = chain.proceed(newRequest)

            Log.d("TAG", "intercept: ${response.code}")
            if (response.code == 401) {
                runBlocking {
                    clearToken()
                }
            }

            return response
        }
    }

}