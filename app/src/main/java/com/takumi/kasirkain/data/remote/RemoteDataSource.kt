package com.takumi.kasirkain.data.remote

import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.google.gson.Gson
import com.takumi.kasirkain.data.remote.request.LoginRequest
import com.takumi.kasirkain.data.remote.response.BaseResponse
import com.takumi.kasirkain.data.remote.response.CategoryResponse
import com.takumi.kasirkain.data.remote.response.ErrorResponse
import com.takumi.kasirkain.data.remote.response.ProductResponse
import com.takumi.kasirkain.domain.model.Product
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): String {
        val response = apiService.login(LoginRequest(username, password))
        if (response.isSuccessful) {
            return response.body()?.token ?: throw Exception("Token tidak ditemukan")
        } else {
            val responseBody = response.errorBody()?.string()
            val message = errorHandling(responseBody)
            throw Exception("Login gagal: $message")
        }
    }

    suspend fun getProduct(category: String?, search: String?): List<ProductResponse> {
        val response = apiService.getProduct(category, search)
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Gagal memuat produk: ${response.message()}")
        }
    }

    suspend fun getCategories(): List<CategoryResponse> {
        val response = apiService.getCategories()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Gagal memuat kategori: ${response.message()}")
        }
    }

    private fun errorHandling(responseBody: String?): String {
        val errorResponse = try {
            if (responseBody != null) {
                val gson = Gson()
                Log.e("errorHandling", "responseBody: $responseBody")
                gson.fromJson(responseBody, ErrorResponse::class.java)
            } else {
                Log.e("Err", "errorHandling: else", )
                null
            }
        } catch (e: Exception) {
            Log.e("Err", "errorHandling: catch", )
            null
        }

        val message = errorResponse?.message ?: "Unknown error"
        return message
    }
}