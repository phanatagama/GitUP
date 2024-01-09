package com.github.core.domain.user.usecase

import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUserUseCase(private val userRepository: UserRepository){
    fun invoke(): Flow<List<UserDetail>>{
        return userRepository.getAllUser()
    }
}