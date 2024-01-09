package com.github.gituser.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.core.domain.user.model.User
import com.github.core.domain.user.usecase.GetAllUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteViewModelModule = module {
    viewModel { FavoriteViewModel(get()) }
}
class FavoriteViewModel constructor(private val getAllUserUseCase: GetAllUserUseCase) :
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
            getAllUserUseCase.invoke().onStart { setLoading() }.catch { exception ->
                hideLoading()
                setError(exception.message.toString())
            }.collect { baseResult ->
                hideLoading()
//            when(baseResult){
//                is BaseResult.Success ->
                if (baseResult.isNotEmpty()) {
                    _state.value = FavoriteActivityState.IsSuccess(baseResult.map { user ->
                        User(
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
    data class IsSuccess(val user: List<User>) : FavoriteActivityState()
}