package com.github.core.data.user.local.database

import androidx.room.*
import com.github.core.data.user.local.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDetailEntity)

    @Delete
    suspend fun delete(user: UserDetailEntity)

    @Query("SELECT * from user ORDER BY username ASC")
    fun getAllUsers(): Flow<List<UserDetailEntity>>
}