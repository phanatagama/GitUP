package com.github.gituser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<GithubResponse>

    @Headers("Authorization: token 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")
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Call<GithubUser>

    @Headers("Authorization: token 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")
    @GET("users/{login}/followers")
    fun getUserFollowers(@Path("login") login: String): Call<GithubResponse>

    @Headers("Authorization: token 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")
    @GET("users/{login}/following")
    fun getUserFollowing(@Path("login") login: String): Call<GithubResponse>
}