package com.github.gituser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Call<GithubUser>

    @GET("users/{login}/followers")
    fun getUserFollowers(@Path("login") login: String): Call<GithubResponse>

    @GET("users/{login}/following")
    fun getUserFollowing(@Path("login") login: String): Call<GithubResponse>
}