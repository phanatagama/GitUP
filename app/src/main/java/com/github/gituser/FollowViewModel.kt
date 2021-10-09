package com.github.gituser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _following = MutableLiveData<Event<List<GithubFollowItem>>>()
    val following: LiveData<Event<List<GithubFollowItem>>> = _following

    private val _follower = MutableLiveData<Event<List<GithubFollowItem>>>()
    val follower: LiveData<Event<List<GithubFollowItem>>> = _follower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<GithubFollowItem>> {
            override fun onResponse(
                call: Call<List<GithubFollowItem>>,
                response: Response<List<GithubFollowItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _follower.value = Event(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<GithubFollowItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<GithubFollowItem>> {
            override fun onResponse(
                call: Call<List<GithubFollowItem>>,
                response: Response<List<GithubFollowItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = Event(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<GithubFollowItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object {
        private const val TAG = "FollowActivity"
    }
}