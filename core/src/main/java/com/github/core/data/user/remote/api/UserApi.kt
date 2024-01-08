package com.github.core.data.user.remote.api

import com.github.core.data.user.remote.dto.GithubFollowItem
import com.github.core.data.user.remote.dto.GithubResponse
import com.github.core.data.user.remote.dto.GithubUser
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    suspend fun getListUsers(@Query("q") q: String): Response<GithubResponse>

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}")
    suspend fun getUserDetail(@Path("login") login: String): Response<GithubUser>

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}/followers")
    suspend fun getUserFollowers(@Path("login") login: String): Response<List<GithubFollowItem>>

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{login}/following")
    suspend fun getUserFollowing(@Path("login") login: String): Response<List<GithubFollowItem>>
}