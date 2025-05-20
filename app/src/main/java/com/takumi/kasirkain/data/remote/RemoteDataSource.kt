package com.takumi.kasirkain.data.remote

import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.google.gson.Gson
import com.takumi.kasirkain.data.remote.request.LoginRequest
import com.takumi.kasirkain.data.remote.response.BaseResponse
import com.takumi.kasirkain.data.remote.response.CategoryResponse
import com.takumi.kasirkain.data.remote.response.ErrorResponse
import com.takumi.kasirkain.data.remote.response.ProductDetailResponse
import com.takumi.kasirkain.data.remote.response.ProductResponse
import com.takumi.kasirkain.data.remote.response.ProductVariantResponse
import com.takumi.kasirkain.data.remote.response.TransactionResponse
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.Transaction
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

    suspend fun getProductVariantDetail(barcode: String): ProductDetailResponse {
        val response = apiService.getProductVariantDetail(barcode)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Data kosong")
        } else {
            if (response.code() == 404) throw Exception("Produk tidak ditemukan")
            throw Exception("Gagal memuat varian produk: ${response.message()}")
        }
    }

    suspend fun getProductVariants(id: Int): List<ProductVariantResponse> {
        val response = apiService.getProductVariants(id)
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Gagal memuat varian produk: ${response.message()}")
        }
    }

    suspend fun getTransactions(): List<TransactionResponse> {
        val response = apiService.getTransactions()
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Data Kosong")
        } else {
            throw Exception("Gagal memuat transaksi: ${response.message()}")
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