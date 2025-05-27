package com.takumi.kasirkain.data.remote

import android.util.Log
import com.google.gson.Gson
import com.takumi.kasirkain.data.remote.model.request.CheckoutRequest
import com.takumi.kasirkain.data.remote.model.request.LoginRequest
import com.takumi.kasirkain.data.remote.model.response.CategoryResponse
import com.takumi.kasirkain.data.remote.model.response.ErrorResponse
import com.takumi.kasirkain.data.remote.model.response.ProductDetailResponse
import com.takumi.kasirkain.data.remote.model.response.ProductResponse
import com.takumi.kasirkain.data.remote.model.response.ProductVariantResponse
import com.takumi.kasirkain.data.remote.model.response.GroupedTransactionResponse
import com.takumi.kasirkain.data.remote.model.response.TransactionHeaderResponse
import com.takumi.kasirkain.data.remote.model.response.UserResponse
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

    suspend fun getTransactions(): List<GroupedTransactionResponse> {
        val response = apiService.getTransactions()
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Data Kosong")
        } else {
            throw Exception("Gagal memuat transaksi: ${response.message()}")
        }
    }

    suspend fun getTransactionById(id: Int) : TransactionHeaderResponse {
        val response = apiService.getTransactionById(id)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("null")
        } else {
            throw Exception("Gagal memuat transaksi: ${response.message()}")
        }
    }

    suspend fun profile(): UserResponse {
        val response = apiService.profile()
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("null")
        } else {
            throw Exception("Gagal memuat profile: ${response.message()}")
        }
    }

    suspend fun checkout(checkoutRequest: CheckoutRequest): TransactionHeaderResponse {
        val response = apiService.checkout(checkoutRequest)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Terjadi kesalahan")
        } else {
            val responseBody = response.errorBody()?.string()
            val message = errorHandling(responseBody)
            throw Exception("Gagal melakukan pembayaran: $message")
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