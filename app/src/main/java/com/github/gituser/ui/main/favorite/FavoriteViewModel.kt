package com.github.gituser.ui.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.usecase.GetAllUserUsecase
import com.github.gituser.ui.main.MainActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val getAllUserUsecase: GetAllUserUsecase) :
    ViewModel() {
    private val _state = MutableStateFlow<FavoriteActivityState>(FavoriteActivityState.Init)
    val state: StateFlow<FavoriteActivityState> get() = _state
    private fun setLoading() {
        _state.value = FavoriteActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = FavoriteActivityState.IsLoading(false)
    }

    private fun setError(message: String) {
        _state.value = FavoriteActivityState.IsError(message)
    }

    init {
        getFavoriteUser()
    }

    private fun getFavoriteUser() {
        viewModelScope.launch {
            getAllUserUsecase.invoke().onStart { setLoading() }.catch { exception ->
                hideLoading()
                setError(exception.message.toString())
            }.collect { baseResult ->
                hideLoading()
//            when(baseResult){
//                is BaseResult.Success ->
                if (!baseResult.isNullOrEmpty()) {
                    _state.value = FavoriteActivityState.IsSuccess(baseResult.map { user ->
                        UserEntity(
                            username = user.username,
                            avatar = user.avatar
                        )
                    })
                } else {
                    _state.value = FavoriteActivityState.IsSuccess(emptyList())
                }
//                is BaseResult.Error -> setError(baseResult.err.message)
//            }
            }
        }
    }
}

sealed class FavoriteActivityState {
    object Init : FavoriteActivityState()
    data class IsLoading(val isLoading: Boolean) : FavoriteActivityState()
    data class IsError(val message: String) : FavoriteActivityState()
    data class IsSuccess(val userEntity: List<UserEntity>) : FavoriteActivityState()
}