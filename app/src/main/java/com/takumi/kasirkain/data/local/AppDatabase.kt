package com.takumi.kasirkain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.takumi.kasirkain.data.local.dao.CartDao
import com.takumi.kasirkain.data.local.dao.TokenDao
import com.takumi.kasirkain.data.local.entity.CartEntity
import com.takumi.kasirkain.data.local.entity.TokenEntity

@Database(entities = [TokenEntity::class, CartEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun cartDao(): CartDao
}