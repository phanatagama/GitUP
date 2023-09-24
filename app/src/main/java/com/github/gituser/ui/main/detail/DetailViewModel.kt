package com.github.gituser.ui.main.detail

import androidx.lifecycle.*
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.usecase.DeleteUserUsecase
import com.github.gituser.domain.user.usecase.GetAllUserUsecase
import com.github.gituser.domain.user.usecase.GetUserDetailUsecase
import com.github.gituser.domain.user.usecase.InsertUserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailActivityState{
    object Init: DetailActivityState()
    data class IsLoading(val isLoading : Boolean) : DetailActivityState()
    data class IsError(val message : String) : DetailActivityState()
    data class IsSuccess(val userDetailEntity: UserDetailEntity?) : DetailActivityState()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getUserDetailUsecase: GetUserDetailUsecase,
    private val getAllUserUsecase: GetAllUserUsecase,
    private val insertUserUsecase: InsertUserUsecase,
    private val deleteUserUsecase: DeleteUserUsecase): ViewModel() {

    private  val _state = MutableStateFlow<DetailActivityState>(DetailActivityState.Init)
    val state: StateFlow<DetailActivityState> get() =_state

//    private val _stateUserFavorite = MutableStateFlow<DetailActivityState>(DetailActivityState.Init)
//    val stateUserFavorite : StateFlow<DetailActivityState> = _stateUserFavorite

    private fun setLoading(){
        _state.value = DetailActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = DetailActivityState.IsLoading(false)
    }

    private fun setError(message: String){
        _state.value = DetailActivityState.IsError(message)
    }

//    private fun setFavoriteLoading(){
//        _stateUserFavorite.value = DetailActivityState.IsLoading(true)
//    }
//
//    private fun hideFavoriteLoading(){
//        _stateUserFavorite.value = DetailActivityState.IsLoading(false)
//    }
//
//    private fun setFavoriteError(message: String){
//        _stateUserFavorite.value = DetailActivityState.IsError(message)
//    }

    fun insertUserDetail(userDetailEntity: UserDetailEntity){
        viewModelScope.launch {
            insertUserUsecase.invoke(userDetailEntity)
        }
//        getAllUser(userDetailEntity.username)
    }

    fun deleteUserDetail(userDetailEntity: UserDetailEntity){
        viewModelScope.launch {
            deleteUserUsecase.invoke(userDetailEntity)
        }
//        getAllUser(userDetailEntity.username)
    }

//    fun getAllUser(username:String){
//        viewModelScope.launch {
//            getAllUserUsecase.invoke().onStart {
//                setFavoriteLoading()
//            }.catch { exception -> hideFavoriteLoading()
//                setFavoriteError(exception.message.toString())
//            }.collect { baseResult ->
//                hideFavoriteLoading()
//                when(baseResult){
//                is BaseResult.Success -> _stateUserFavorite.value = DetailActivityState.IsSuccess(
//                    baseResult.data.firstOrNull { it.username == username })
//                is BaseResult.Error -> setError(baseResult.err.message)
//            } }
//        }
//    }

    fun getUserDetail(username:String){
        viewModelScope.launch {
            combine(getUserDetailUsecase.invoke(username),getAllUserUsecase.invoke()){userDetail, favoriteUser ->
                if(userDetail is BaseResult.Success){
                    val isFavorite = favoriteUser.firstOrNull{ it.username == username} != null
                    BaseResult.Success(userDetail.data.copy(isFavorite = isFavorite))
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
                when(baseResult) {
                    is BaseResult.Success -> _state.value = DetailActivityState.IsSuccess(baseResult.data)
                    is BaseResult.Error -> setError(baseResult.err.message)
                }
            }
//            getUserDetailUsecase.invoke(username).onStart {
//                setLoading()
//            }.catch { exception -> hideLoading()
//            setError(exception.message.toString())
//            }.collect { baseResult ->
//                hideLoading()
//                when(baseResult) {
//                  is BaseResult.Success -> _state.value = DetailActivityState.IsSuccess(baseResult.data)
//                     is BaseResult.Error -> setError(baseResult.err.message)
//                }
//            }
        }
    }

    companion object {
        private const val TAG = "DETAIL_VIEW_MODEL_TAG"
    }
}