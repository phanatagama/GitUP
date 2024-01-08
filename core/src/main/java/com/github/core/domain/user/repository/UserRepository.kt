package com.github.core.domain.user.repository

import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.User
import com.github.core.domain.user.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    // remote
    fun getUsersByQuery(q:String) : Flow<BaseResult<List<User>, Failure>>
    fun getUserFollowing(login:String) : Flow<BaseResult<List<User>, Failure>>
    fun getUserFollowers(login:String) : Flow<BaseResult<List<User>, Failure>>
    fun getUserDetail(login:String) : Flow<BaseResult<UserDetail, Failure>>

    // local
    fun getAllUser() : Flow<List<UserDetail>>
    suspend fun insertUser(userDetail: UserDetail) : BaseResult<Unit, Failure>
    suspend fun deleteUser(userDetail: UserDetail) : BaseResult<Unit, Failure>
    suspend fun saveTheme(isDarkMode:Boolean)
    fun getTheme() : Flow<Boolean>
}