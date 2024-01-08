package com.github.core.domain.user.usecase

import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.repository.UserRepository

class InsertUserUsecase constructor(private val userRepository: UserRepository){
    suspend fun invoke(user: UserDetail) : BaseResult<Unit, Failure> {
        return userRepository.insertUser(user)
    }
    companion object
}