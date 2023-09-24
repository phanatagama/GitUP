package com.github.gituser.data.user.remote

import android.util.Log
import com.github.gituser.data.user.remote.api.UserApi

import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.entity.UserEntity


class UserRemoteDataSource constructor(private val userApi: UserApi){
    companion object{
        const val TAG ="USER_REMOTE_DATA"
    }
    suspend fun getListUsers(q: String): BaseResult<List<UserEntity>, Failure> {
        return try {
            val response = userApi.getListUsers(q)
//            Log.e(TAG, "getListUsers: $response")
            if (response.isSuccessful){
                val userEntity = response.body()?.items?.map { item -> UserEntity(item.login, item.avatarUrl) } ?: listOf()
                BaseResult.Success(userEntity)
            } else{
                BaseResult.Error(Failure(code = response.code(), message = response.message()))
            }
        } catch (e: Exception){
            BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }


    suspend fun getUserDetail(login: String) : BaseResult<UserDetailEntity, Failure> {
        return try {
            val response = userApi.getUserDetail(login)
            if (response.isSuccessful){
                val body =  response.body()
                val userDetailEntity = UserDetailEntity(
                    name = body?.name as String?,
                    username = body?.login as String,
                    avatar = body.avatarUrl,
                    company = body.company,
                    followers = body.followers,
                    following = body.following,
                    location = body.location,
                    repository = body.publicRepos
                )
                BaseResult.Success(userDetailEntity)
            } else{
                BaseResult.Error(Failure(code = response.code(), message = response.message().toString()))
            }
        }catch (e: Exception){
            BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }


    suspend fun getUserFollowers( login: String) : BaseResult<List<UserEntity>, Failure> {
        return try {
            val response = userApi.getUserFollowers(login)
            if (response.isSuccessful){
                val userEntity = response.body()?.map { item -> UserEntity(item.login, item.avatarUrl) } ?: listOf()
                BaseResult.Success(userEntity)
            } else{
                BaseResult.Error(Failure(code = response.code(), message = response.message()))
            }
        } catch (e: Exception){
            BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }


    suspend fun getUserFollowing( login: String) : BaseResult<List<UserEntity>, Failure> {
        return try {
            val response = userApi.getUserFollowing(login)
            if (response.isSuccessful){
                val userEntity = response.body()?.map { item -> UserEntity(item.login, item.avatarUrl) } ?: listOf()
                BaseResult.Success(userEntity)
            } else{
                BaseResult.Error(Failure(code = response.code(), message = response.message()))
            }
        } catch (e: Exception){
            BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}