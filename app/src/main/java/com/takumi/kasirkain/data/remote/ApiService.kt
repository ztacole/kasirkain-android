package com.takumi.kasirkain.data.remote

import com.takumi.kasirkain.data.remote.model.request.CheckoutRequest
import com.takumi.kasirkain.data.remote.model.request.LoginRequest
import com.takumi.kasirkain.data.remote.model.response.BaseResponse
import com.takumi.kasirkain.data.remote.model.response.CategoryResponse
import com.takumi.kasirkain.data.remote.model.response.LoginResponse
import com.takumi.kasirkain.data.remote.model.response.ProductDetailResponse
import com.takumi.kasirkain.data.remote.model.response.ProductResponse
import com.takumi.kasirkain.data.remote.model.response.ProductVariantResponse
import com.takumi.kasirkain.data.remote.model.response.GroupedTransactionResponse
import com.takumi.kasirkain.data.remote.model.response.TransactionHeaderResponse
import com.takumi.kasirkain.data.remote.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("products")
    suspend fun getProduct(
        @Query("category") category: String?,
        @Query("search") search: String?
    ): Response<BaseResponse<List<ProductResponse>>>

    @GET("categories")
    suspend fun getCategories(): Response<BaseResponse<List<CategoryResponse>>>

    @GET("product-variant/detail/{barcode}")
    suspend fun getProductVariantDetail(
        @Path("barcode") barcode: String
    ): Response<BaseResponse<ProductDetailResponse>>

    @GET("product-variants/{id}")
    suspend fun getProductVariants(
        @Path("id") id: Int
    ): Response<BaseResponse<List<ProductVariantResponse>>>

    @GET("transactions")
    suspend fun getTransactions(): Response<BaseResponse<List<GroupedTransactionResponse>>>

    @GET("transaction/{id}")
    suspend fun getTransactionById(
        @Path("id") id: Int
    ): Response<BaseResponse<TransactionHeaderResponse>>

    @POST("checkout")
    suspend fun checkout(
        @Body request: CheckoutRequest
    ): Response<BaseResponse<TransactionHeaderResponse>>

    @GET("profile")
    suspend fun profile() : Response<BaseResponse<UserResponse>>

    @POST("logout")
    suspend fun logout()
}