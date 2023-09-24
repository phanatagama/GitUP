package com.github.gituser.ui.main

import GithubFollowItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.usecase.GetUserFollowersUsecase
import com.github.gituser.domain.user.usecase.GetUserFollowingUsecase
import com.github.gituser.ui.common.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(private val getUserFollowersUsecase: GetUserFollowersUsecase,
                                          private val getUserFollowingUsecase: GetUserFollowingUsecase) : ViewModel() {
    private val _stateFollower = MutableStateFlow<FollowFragmentState>(FollowFragmentState.Init)
    val stateFollower : StateFlow<FollowFragmentState> = _stateFollower

    private val _stateFollowing = MutableStateFlow<FollowFragmentState>(FollowFragmentState.Init)
    val stateFollowing : StateFlow<FollowFragmentState> = _stateFollowing

    private fun setFollowerLoading(){
        _stateFollower.value = FollowFragmentState.IsLoading(true)
    }
    private fun hideFollowerLoading(){
        _stateFollower.value = FollowFragmentState.IsLoading(false)
    }
    private fun setFollowerError(message:String){
        _stateFollower.value = FollowFragmentState.IsError(message)
    }
    private fun setFollowingLoading(){
        _stateFollowing.value = FollowFragmentState.IsLoading(true)
    }
    private fun hideFollowingLoading(){
        _stateFollowing.value = FollowFragmentState.IsLoading(false)
    }
    private fun setFollowingError(message:String){
        _stateFollowing.value = FollowFragmentState.IsError(message)
    }

    fun getUserFollower(username:String){
        viewModelScope.launch {
            getUserFollowersUsecase.invoke(username).onStart {
                setFollowerLoading()
            }.catch { exception -> hideFollowerLoading()
            setFollowerError(exception.message.toString())
            }.collect { baseResult ->
                hideFollowerLoading()
                Log.d(TAG, "getUserFollower: $baseResult")
                when(baseResult){
                    is BaseResult.Success -> _stateFollower.value = FollowFragmentState.IsSuccess(baseResult.data)
                    is BaseResult.Error -> setFollowerError(baseResult.err.message)
                }
            }
        }
    }
    fun getUserFollowing(username:String){
        viewModelScope.launch {
            getUserFollowingUsecase.invoke(username).onStart {
                setFollowingLoading()
            }.catch { exception -> hideFollowingLoading()
            setFollowingError(exception.message.toString())
            }.collect { baseResult ->
                hideFollowingLoading()
                    Log.d(TAG, "getUserFollowing: $baseResult")
                when(baseResult){
                    is BaseResult.Error -> setFollowingError(baseResult.err.message)
                    is BaseResult.Success -> _stateFollowing.value = FollowFragmentState.IsSuccess(baseResult.data)
                }
            }
        }
    }
//    private val _following = MutableLiveData<Event<List<GithubFollowItem>>>()
//    val following: LiveData<Event<List<GithubFollowItem>>> = _following
//
//    private val _follower = MutableLiveData<Event<List<GithubFollowItem>>>()
//    val follower: LiveData<Event<List<GithubFollowItem>>> = _follower
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getFollower(username: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getUserFollowers(username)
//        client.enqueue(object : Callback<List<GithubFollowItem>> {
//            override fun onResponse(
//                call: Call<List<GithubFollowItem>>,
//                response: Response<List<GithubFollowItem>>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _follower.value = Event(responseBody)
//                    }
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<List<GithubFollowItem>>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }
//
//    fun getFollowing(username: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getUserFollowing(username)
//        client.enqueue(object : Callback<List<GithubFollowItem>> {
//            override fun onResponse(
//                call: Call<List<GithubFollowItem>>,
//                response: Response<List<GithubFollowItem>>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        _following.value = Event(responseBody)
//                    }
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<List<GithubFollowItem>>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }
    companion object {
        private const val TAG = "FOLLOW_VIEW_MODEL_TAG"
    }
}

sealed class FollowFragmentState{
    object Init: FollowFragmentState()
    data class IsLoading(val isLoading:Boolean) : FollowFragmentState()
    data class IsError(val message: String) : FollowFragmentState()
    data class IsSuccess(val data: List<UserEntity>): FollowFragmentState()
}