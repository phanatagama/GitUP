package com.github.core.data.user.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.core.data.user.local.entity.UserDetailEntity

@Database(entities = [UserDetailEntity::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}