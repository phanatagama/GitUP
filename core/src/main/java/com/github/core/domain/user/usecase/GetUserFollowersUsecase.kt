package com.github.core.domain.user.usecase

import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.User
import com.github.core.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserFollowersUsecase(private val userRepository: UserRepository) {
    fun invoke(login: String): Flow<BaseResult<List<User>, Failure>> {
        return userRepository.getUserFollowers(login)
    }
}
