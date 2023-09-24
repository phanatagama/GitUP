package com.github.gituser.data.user

import android.util.Log
import com.github.gituser.data.user.local.UserLocalDataSource
import com.github.gituser.data.user.remote.UserRemoteDataSource
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userRemoteDataSource:UserRemoteDataSource, private val userLocalDataSource: UserLocalDataSource ) : UserRepository{
    override suspend fun getUsersByQuery(q: String): Flow<BaseResult<List<UserEntity>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getListUsers(q)
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserFollowing(login: String): Flow<BaseResult<List<UserEntity>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserFollowing(login)
            Log.d(TAG, "getUserFollowing: $remoteResult")
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserFollowers(login: String): Flow<BaseResult<List<UserEntity>, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserFollowers(login)
            Log.d(TAG, "getUserFollowers: $remoteResult")
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserDetail(login: String): Flow<BaseResult<UserDetailEntity, Failure>> {
        return flow {
            val remoteResult = userRemoteDataSource.getUserDetail(login)
            emit(remoteResult)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertUser(userDetailEntity: UserDetailEntity) : BaseResult<Unit,Failure>{
        userLocalDataSource.insert(userDetailEntity)
        return BaseResult.Success(Unit)
    }

    suspend fun updateUser(userDetailEntity: UserDetailEntity){
        userLocalDataSource.update(userDetailEntity)
    }

    override fun getAllUser() : Flow<List<UserDetailEntity>>{
        return userLocalDataSource.getAllUser()
    }

    override suspend fun deleteUser(userDetailEntity: UserDetailEntity) : BaseResult<Unit,Failure>{
        userLocalDataSource.delete(userDetailEntity)
        return BaseResult.Success(Unit)
    }
    companion object{
        const val  TAG = "USER_REPOSITORY"
    }
}