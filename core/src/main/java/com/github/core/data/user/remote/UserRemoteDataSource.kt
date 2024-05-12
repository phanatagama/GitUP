package com.github.core.data.user.remote

import android.util.Log
import com.github.core.data.user.remote.api.UserApi
import com.github.core.data.user.remote.dto.mapToDomain
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.User
import com.github.core.domain.user.model.UserDetail


class UserRemoteDataSource constructor(private val userApi: UserApi) {
    companion object {
        const val TAG = "USER_REMOTE_DATA"
    }

    suspend fun getListUsers(q: String): BaseResult<List<User>, Failure> {
        return try {
            val response = userApi.getListUsers(q)
            Log.d(TAG, "getListUsers: $response")
            if (response.isSuccessful) {
                val userEntity = response.body()?.items?.map { it.mapToDomain() } ?: listOf()
                BaseResult.Success(userEntity)
            } else {
            Log.d("ERROR:", response.message())
                BaseResult.Error(
                    Failure(
                        code = response.code(),
                        message = response.message()
                    )
                )
            }
        } catch (e: Exception) {
            Log.d("ERROR:", e.message.toString())
            BaseResult.Error(
                Failure(
                    -1,
                    e.message.toString()
                )
            )
        }
    }


    suspend fun getUserDetail(login: String): BaseResult<UserDetail, Failure> {
        return try {
            val response = userApi.getUserDetail(login)
            if (response.isSuccessful) {
                val body = response.body()
                val userDetail = UserDetail(
                    name = body?.name as String?,
                    username = body?.login as String,
                    avatar = body.avatarUrl,
                    company = body.company,
                    followers = body.followers,
                    following = body.following,
                    location = body.location,
                    repository = body.publicRepos
                )
                BaseResult.Success(userDetail)
            } else {
                BaseResult.Error(
                    Failure(
                        code = response.code(),
                        message = response.message().toString()
                    )
                )
            }
        } catch (e: Exception) {
            BaseResult.Error(
                Failure(
                    -1,
                    e.message.toString()
                )
            )
        }
    }


    suspend fun getUserFollowers(login: String): BaseResult<List<User>, Failure> {
        return try {
            val response = userApi.getUserFollowers(login)
            if (response.isSuccessful) {
                val result = response.body()?.map { it.mapToDomain() } ?: listOf()
                BaseResult.Success(result)
            } else {
                BaseResult.Error(
                    Failure(
                        code = response.code(),
                        message = response.message()
                    )
                )
            }
        } catch (e: Exception) {
            BaseResult.Error(
                Failure(
                    -1,
                    e.message.toString()
                )
            )
        }
    }


    suspend fun getUserFollowing(login: String): BaseResult<List<User>, Failure> {
        return try {
            val response = userApi.getUserFollowing(login)
            if (response.isSuccessful) {
                val result = response.body()?.map { it.mapToDomain() } ?: listOf()
                BaseResult.Success(result)
            } else {
                BaseResult.Error(
                    Failure(
                        code = response.code(),
                        message = response.message()
                    )
                )
            }
        } catch (e: Exception) {
            BaseResult.Error(
                Failure(
                    -1,
                    e.message.toString()
                )
            )
        }
    }
}