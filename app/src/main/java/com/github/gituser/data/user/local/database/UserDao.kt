package com.github.gituser.data.user.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.gituser.domain.user.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDetailEntity)

    @Update
    suspend fun update(user: UserDetailEntity)

    @Delete
    suspend fun delete(user: UserDetailEntity)

    @Query("SELECT * from user ORDER BY username ASC")
    fun getAllUsers(): Flow<List<UserDetailEntity>>
}