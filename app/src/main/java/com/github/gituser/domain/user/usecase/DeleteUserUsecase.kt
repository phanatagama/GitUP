package com.github.gituser.domain.user.usecase

import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserUsecase @Inject constructor(private val userRepository: UserRepository){
    suspend fun invoke(userDetailEntity: UserDetailEntity): BaseResult<Unit, Failure> {
        return userRepository.deleteUser(userDetailEntity)
    }
}