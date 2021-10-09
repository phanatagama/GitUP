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
    fun getUserFollowers(@Path("login") login: String): Call<List<GithubFollowItem>>

    @Headers("Authorization: token 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")
    @GET("users/{login}/following")
    fun getUserFollowing(@Path("login") login: String): Call<List<GithubFollowItem>>
}