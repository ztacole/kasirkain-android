package com.takumi.kasirkain.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.takumi.kasirkain.data.local.entity.TokenEntity

@Dao
interface TokenDao {
    @Query("SELECT * FROM token WHERE id = 1")
    suspend fun getToken(): TokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(token: TokenEntity)

    @Query("DELETE FROM token")
    suspend fun clearToken()
}