package com.github.gituser.domain.user.usecase

import android.util.Log
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUserUsecase @Inject constructor(private val userRepository: UserRepository){
    suspend fun invoke(user: UserDetailEntity) : BaseResult<Unit, Failure> {
//        Log.d(TAG, "invoke: DISINI USECASE")
        return userRepository.insertUser(user)
    }
    companion object{
        const val TAG = "INSERTUSECASE"
    }
}