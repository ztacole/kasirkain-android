package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.remote.ApiService
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.data.remote.request.LoginRequest
import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
): AuthRepository {
    override suspend fun login(username: String, password: String): String {
        val token =  remote.login(username, password)
        local.saveToken(token)
        return token
    }
}