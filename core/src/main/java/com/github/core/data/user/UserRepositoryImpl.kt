package com.github.core.data.user

import android.util.Log
import com.github.core.data.common.SettingPreferences
import com.github.core.data.user.local.UserLocalDataSource
import com.github.core.data.user.local.entity.mapToDomain
import com.github.core.data.user.remote.UserRemoteDataSource
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.User
import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val preferences: SettingPreferences
) :
    UserRepository {
    override fun getUsersByQuery(q: String): Flow<BaseResult<List<User>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getListUsers(q)
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveTheme(isDarkMode: Boolean) {
        preferences.saveThemeSetting(isDarkMode)
    }

    override fun getTheme(): Flow<Boolean> {
        return preferences.getThemeSetting()
    }

    override fun getUserFollowing(login: String): Flow<BaseResult<List<User>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserFollowing(login)
            Log.d(TAG, "getUserFollowing: $remoteResult")
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserFollowers(login: String): Flow<BaseResult<List<User>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserFollowers(login)
            Log.d(TAG, "getUserFollowers: $remoteResult")
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserDetail(login: String): Flow<BaseResult<UserDetail, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserDetail(login)
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertUser(userDetail: UserDetail): BaseResult<Unit, Failure> {
        userLocalDataSource.insert(userDetail.mapToEntity())
        return BaseResult.Success(Unit)
    }

    override fun getAllUser(): Flow<List<UserDetail>> {
        return userLocalDataSource.getAllUser().map { data -> data.map { it.mapToDomain() } }
    }

    override suspend fun deleteUser(userDetail: UserDetail): BaseResult<Unit, Failure> {
        userLocalDataSource.delete(userDetail.mapToEntity())
        return BaseResult.Success(Unit)
    }

    companion object {
        const val TAG = "USER_REPOSITORY"
    }
}