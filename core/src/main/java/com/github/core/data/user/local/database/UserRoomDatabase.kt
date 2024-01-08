package com.github.core.data.user.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.core.data.user.local.entity.UserDetailEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [UserDetailEntity::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null
        private val passphrase: ByteArray = SQLiteDatabase.getBytes("user".toCharArray())
        private val factory = SupportFactory(passphrase)

        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserRoomDatabase::class.java, "user_database")
                        .fallbackToDestructiveMigration()
                        .openHelperFactory(factory)
                        .build()
                }
            }
            return INSTANCE as UserRoomDatabase
        }
    }
}