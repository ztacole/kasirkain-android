package com.takumi.kasirkain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.takumi.kasirkain.data.local.dao.TokenDao
import com.takumi.kasirkain.data.local.entity.TokenEntity

@Database(entities = [TokenEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}