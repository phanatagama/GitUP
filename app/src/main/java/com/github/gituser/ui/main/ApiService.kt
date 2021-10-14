package com.github.gituser.ui.main

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<GithubResponse>

//    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Call<GithubUser>

//    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{login}/followers")
    fun getUserFollowers(@Path("login") login: String): Call<List<GithubFollowItem>>

//    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{login}/following")
    fun getUserFollowing(@Path("login") login: String): Call<List<GithubFollowItem>>
}