package com.github.gituser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.core.domain.common.usecase.GetThemeUsecase
import com.github.core.domain.common.usecase.SaveThemeUsecase
import com.github.core.domain.user.model.User
import com.github.core.domain.user.usecase.GetUsersByQueryUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel constructor(
    private val getUsersByQueryUsecase: GetUsersByQueryUsecase,
    private val getThemeUsecase: GetThemeUsecase,
    private val saveThemeUsecase: SaveThemeUsecase
) : ViewModel() {
    private val _state = MutableStateFlow<MainActivityState>(MainActivityState.Init)
    val state: StateFlow<MainActivityState> get() = _state
    private fun setLoading() {
        _state.value = MainActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = MainActivityState.IsLoading(false)
    }

    private fun setError(message: String) {
        _state.value = MainActivityState.IsError(message)
    }

    init {
        getUsersByQuery(getRandomString())
    }

    fun getUsersByQuery(q: String) {
        viewModelScope.launch {
            getUsersByQueryUsecase.invoke(q).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                setError(exception.message.toString())
            }.collect { baseResult ->
                hideLoading()
                when (baseResult) {
                    is com.github.core.domain.common.base.BaseResult.Error -> setError(baseResult.err.message)
                    is com.github.core.domain.common.base.BaseResult.Success -> _state.value =
                        MainActivityState.IsSuccess(baseResult.data)
                }
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return getThemeUsecase.invoke().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            saveThemeUsecase.invoke(isDarkModeActive)
        }
    }

    private fun getRandomString(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return charset.random().toString()
    }

    companion object
}

sealed class MainActivityState {
    object Init : MainActivityState()
    data class IsLoading(val isLoading: Boolean) : MainActivityState()
    data class IsError(val message: String) : MainActivityState()
    data class IsSuccess(val userEntity: List<User>) : MainActivityState()
}