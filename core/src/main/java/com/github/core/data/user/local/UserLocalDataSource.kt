package com.github.core.data.user.local

import com.github.core.data.user.local.database.UserDao
import com.github.core.data.user.local.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

class UserLocalDataSource constructor(private val userDao: UserDao) {
    companion object;
    suspend fun insert(userDetailEntity: UserDetailEntity){
        userDao.insert(userDetailEntity)
    }


    suspend fun delete(userDetailEntity: UserDetailEntity) = userDao.delete(userDetailEntity)


    fun getAllUser(): Flow<List<UserDetailEntity>> = userDao.getAllUsers()

}