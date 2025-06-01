package com.takumi.kasirkain.di

import android.content.Context
import androidx.room.Room
import com.takumi.kasirkain.data.local.TokenManager
import com.takumi.kasirkain.data.local.AppDatabase
import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.dao.CartDao
import com.takumi.kasirkain.data.remote.ApiService
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.data.remote.TokenInterceptor
import com.takumi.kasirkain.data.repository.AuthRepositoryImpl
import com.takumi.kasirkain.data.repository.CartRepositoryImpl
import com.takumi.kasirkain.data.repository.CategoryRepositoryImpl
import com.takumi.kasirkain.data.repository.ProductRepositoryImpl
import com.takumi.kasirkain.data.repository.TransactionRepositoryImpl
import com.takumi.kasirkain.domain.repository.AuthRepository
import com.takumi.kasirkain.domain.repository.CartRepository
import com.takumi.kasirkain.domain.repository.CategoryRepository
import com.takumi.kasirkain.domain.repository.ProductRepository
import com.takumi.kasirkain.domain.repository.TransactionRepository
import com.takumi.kasirkain.presentation.util.PrinterManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl(): String = "https://backend24.site/Rian/XI/takumi/kasirkain/api/api/"

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
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager = TokenManager(context)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        db: AppDatabase,
        tokenManager: TokenManager
    ): LocalDataSource = LocalDataSource(db, tokenManager)

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
        .addInterceptor(
            TokenInterceptor(
                tokenProvider =  { localDataSource.getToken() },
                clearToken = { localDataSource.clearToken() }
            )
        )
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

    @Provides
    @Singleton
    fun providePrinterManager(
        @ApplicationContext context: Context
    ): PrinterManager = PrinterManager(context)
}