package com.github.gituser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _users = MutableLiveData<Event<GithubUser>>()
    val users: LiveData<Event<GithubUser>> = _users

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading


    fun getUser(username: String) {
        _isLoading.value = Event(true)
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<GithubUser> {
            override fun onResponse(
                call: Call<GithubUser>,
                response: Response<GithubUser>
            ) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = Event(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                _isLoading.value = Event(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}