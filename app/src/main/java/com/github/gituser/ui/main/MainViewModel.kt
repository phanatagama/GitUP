package com.github.gituser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.usecase.GetUsersByQueryUsecase
import com.github.gituser.ui.common.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getUsersByQueryUsecase: GetUsersByQueryUsecase, private val pref: SettingPreferences): ViewModel() {
    private  val _state = MutableStateFlow<MainActivityState>(MainActivityState.Init)
    val state: StateFlow<MainActivityState> get() =_state
    private fun setLoading(){
        _state.value = MainActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = MainActivityState.IsLoading(false)
    }

    private fun setError(message: String){
        _state.value = MainActivityState.IsError(message)
    }

    init {
        getUsersByQuery(getRandomString(1))
    }

    fun getUsersByQuery(q:String){
        viewModelScope.launch {
            getUsersByQueryUsecase.invoke(q).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                setError(exception.message.toString())
            }.collect{ baseResult ->
                hideLoading()
                when(baseResult){
                    is BaseResult.Error -> setError(baseResult.err.message)
                    is BaseResult.Success -> _state.value = MainActivityState.IsSuccess(baseResult.data)
                }
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    private fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

sealed class MainActivityState  {
    object Init : MainActivityState()
    data class IsLoading(val isLoading: Boolean) : MainActivityState()
    data class IsError(val message: String) : MainActivityState()
    data class IsSuccess(val userEntity: List<UserEntity>) : MainActivityState()
}