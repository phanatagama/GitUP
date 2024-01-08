package com.github.gituser.ui.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.usecase.DeleteUserUsecase
import com.github.core.domain.user.usecase.GetAllUserUsecase
import com.github.core.domain.user.usecase.GetUserDetailUsecase
import com.github.core.domain.user.usecase.InsertUserUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class DetailActivityState {
    object Init : DetailActivityState()
    data class IsLoading(val isLoading: Boolean) : DetailActivityState()
    data class IsError(val message: String) : DetailActivityState()
    data class IsSuccess(val userDetail: UserDetail?) : DetailActivityState()
}

class DetailViewModel(
    private val getUserDetailUsecase: GetUserDetailUsecase,
    private val getAllUserUsecase: GetAllUserUsecase,
    private val insertUserUsecase: InsertUserUsecase,
    private val deleteUserUsecase: DeleteUserUsecase
) : ViewModel() {

    private val _state = MutableStateFlow<DetailActivityState>(DetailActivityState.Init)
    val state: StateFlow<DetailActivityState> get() = _state

    private fun setLoading() {
        _state.value = DetailActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = DetailActivityState.IsLoading(false)
    }

    private fun setError(message: String) {
        _state.value = DetailActivityState.IsError(message)
    }

    fun insertUserDetail(userDetail: UserDetail) {
        viewModelScope.launch {
            insertUserUsecase.invoke(userDetail)
        }
    }

    fun deleteUserDetail(userDetail: UserDetail) {
        viewModelScope.launch {
            deleteUserUsecase.invoke(userDetail)
        }
    }

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            combine(
                getUserDetailUsecase.invoke(username),
                getAllUserUsecase.invoke()
            ) { userDetail, favoriteUser ->
                if (userDetail is BaseResult.Success) {
                    val isFavorite = favoriteUser.firstOrNull { it.username == username } != null
                    BaseResult.Success(
                        userDetail.data.copy(
                            isFavorite = isFavorite
                        )
                    )
                } else {
                    userDetail
                }
            }.onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                setError(exception.message.toString())
            }.collect { baseResult ->
                hideLoading()
                when (baseResult) {
                    is BaseResult.Success -> _state.value =
                        DetailActivityState.IsSuccess(baseResult.data)

                    is BaseResult.Error -> setError(baseResult.err.message)
                }
            }
        }
    }
}