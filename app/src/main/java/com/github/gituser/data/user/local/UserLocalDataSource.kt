package com.github.gituser.data.user.local

import android.util.Log
import com.github.gituser.data.user.local.database.UserDao
import com.github.gituser.domain.user.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

class UserLocalDataSource constructor(private val userDao: UserDao) {
    companion object{
        const val TAG = "USER_LOCAL_DATA_SOURCE"
    }
    suspend fun insert(userDetailEntity: UserDetailEntity){
//        Log.d(TAG, "insert: $userDetailEntity")
        userDao.insert(userDetailEntity)
    }

    suspend fun update(userDetailEntity: UserDetailEntity) = userDao.update(userDetailEntity)


    suspend fun delete(userDetailEntity: UserDetailEntity) = userDao.delete(userDetailEntity)


    fun getAllUser(): Flow<List<UserDetailEntity>> = userDao.getAllUsers()

}