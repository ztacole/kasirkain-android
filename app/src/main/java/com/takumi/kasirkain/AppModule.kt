package com.takumi.kasirkain

import android.content.Context
import androidx.room.Room
import com.takumi.kasirkain.data.local.AppDatabase
import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.dao.CartDao
import com.takumi.kasirkain.data.local.dao.TokenDao
import com.takumi.kasirkain.data.remote.ApiService
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.data.remote.TokenInterceptor
import com.takumi.kasirkain.data.repository.AuthRepositoryImpl
import com.takumi.kasirkain.data.repository.CartRepositoryImpl
import com.takumi.kasirkain.data.repository.CategoryRepositoryImpl
import com.takumi.kasirkain.data.repository.ProductRepositoryImpl
import com.takumi.kasirkain.data.repository.TokenRepositoryImpl
import com.takumi.kasirkain.data.repository.TransactionRepositoryImpl
import com.takumi.kasirkain.domain.repository.AuthRepository
import com.takumi.kasirkain.domain.repository.CartRepository
import com.takumi.kasirkain.domain.repository.CategoryRepository
import com.takumi.kasirkain.domain.repository.ProductRepository
import com.takumi.kasirkain.domain.repository.TokenRepository
import com.takumi.kasirkain.domain.repository.TransactionRepository
import com.takumi.kasirkain.domain.usecase.GetProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.lang.Class

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    // Local
    fun provideBaseUrl(): String = "http://10.0.2.2:8000/api/"
    // TP-LINK_2699
//    fun provideBaseUrl(): String = "http://192.168.1.137:8000/api/"
    // Home
//    fun provideBaseUrl(): String = "http://192.168.1.8:8000/api/"
    // TP-LINK_222B
//    fun provideBaseUrl(): String = "http://192.168.1.105:8000/api/"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "kasirkain_db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideLocalDataSource(
        db: AppDatabase
    ): LocalDataSource = LocalDataSource(db)

    @Provides
    @Singleton
    fun provideTokenDao(
        db: AppDatabase
    ): TokenDao = db.tokenDao()

    @Provides
    @Singleton
    fun provideCartDao(
        db: AppDatabase
    ): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideOkHttp(
        localDataSource: LocalDataSource
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor(tokenProvider =  {
            localDataSource.getToken()
        }, clearToken = {localDataSource.clearToken()}))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: ApiService
    ): RemoteDataSource = RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideAuthRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): AuthRepository = AuthRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun provideTokenRepository(
        local: LocalDataSource
    ): TokenRepository = TokenRepositoryImpl(local)

    @Provides
    @Singleton
    fun provideProductRepository(
        remote: RemoteDataSource,
        local: LocalDataSource
    ): ProductRepository = ProductRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        remote: RemoteDataSource
    ): CategoryRepository = CategoryRepositoryImpl(remote)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        remote: RemoteDataSource
    ): TransactionRepository = TransactionRepositoryImpl(remote)

    @Provides
    @Singleton
    fun provideCartRepository(
        local: LocalDataSource
    ): CartRepository = CartRepositoryImpl(local)
}