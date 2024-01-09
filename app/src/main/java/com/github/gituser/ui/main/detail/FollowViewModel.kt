package com.github.gituser.ui.main.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.user.model.User
import com.github.core.domain.user.usecase.GetUserFollowersUseCase
import com.github.core.domain.user.usecase.GetUserFollowingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FollowViewModel(
    private val getUserFollowersUseCase: GetUserFollowersUseCase,
    private val getUserFollowingUseCase: GetUserFollowingUseCase
) : ViewModel() {
    private val _stateFollower = MutableStateFlow<FollowFragmentState>(FollowFragmentState.Init)
    val stateFollower: StateFlow<FollowFragmentState> = _stateFollower

    private val _stateFollowing = MutableStateFlow<FollowFragmentState>(FollowFragmentState.Init)
    val stateFollowing: StateFlow<FollowFragmentState> = _stateFollowing

    private fun setFollowerLoading() {
        _stateFollower.value = FollowFragmentState.IsLoading(true)
    }

    private fun hideFollowerLoading() {
        _stateFollower.value = FollowFragmentState.IsLoading(false)
    }

    private fun setFollowerError(message: String) {
        _stateFollower.value = FollowFragmentState.IsError(message)
    }

    private fun setFollowingLoading() {
        _stateFollowing.value = FollowFragmentState.IsLoading(true)
    }

    private fun hideFollowingLoading() {
        _stateFollowing.value = FollowFragmentState.IsLoading(false)
    }

    private fun setFollowingError(message: String) {
        _stateFollowing.value = FollowFragmentState.IsError(message)
    }

    fun getUserFollower(username: String) {
        viewModelScope.launch {
            getUserFollowersUseCase.invoke(username).onStart {
                setFollowerLoading()
            }.catch { exception ->
                hideFollowerLoading()
                setFollowerError(exception.message.toString())
            }.collect { baseResult ->
                hideFollowerLoading()
                Log.d(TAG, "getUserFollower: $baseResult")
                when (baseResult) {
                    is BaseResult.Success -> _stateFollower.value =
                        FollowFragmentState.IsSuccess(baseResult.data)

                    is BaseResult.Error -> setFollowerError(
                        baseResult.err.message
                    )
                }
            }
        }
    }

    fun getUserFollowing(username: String) {
        viewModelScope.launch {
            getUserFollowingUseCase.invoke(username).onStart {
                setFollowingLoading()
            }.catch { exception ->
                hideFollowingLoading()
                setFollowingError(exception.message.toString())
            }.collect { baseResult ->
                hideFollowingLoading()
                Log.d(TAG, "getUserFollowing: $baseResult")
                when (baseResult) {
                    is BaseResult.Error -> setFollowingError(
                        baseResult.err.message
                    )
                    is BaseResult.Success -> _stateFollowing.value =
                        FollowFragmentState.IsSuccess(baseResult.data)
                }
            }
        }
    }

    companion object {
        private const val TAG = "FOLLOW_VIEW_MODEL_TAG"
    }
}

sealed class FollowFragmentState {
    object Init : FollowFragmentState()
    data class IsLoading(val isLoading: Boolean) : FollowFragmentState()
    data class IsError(val message: String) : FollowFragmentState()
    data class IsSuccess(val data: List<User>) : FollowFragmentState()
}