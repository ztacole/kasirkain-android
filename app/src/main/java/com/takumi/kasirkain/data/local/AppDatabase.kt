package com.takumi.kasirkain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.takumi.kasirkain.data.local.dao.CartDao
import com.takumi.kasirkain.data.local.entity.CartEntity

@Database(entities = [CartEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}