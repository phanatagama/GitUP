package com.github.gituser.domain.user.usecase

import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFollowersUsecase @Inject constructor(private val userRepository: UserRepository){
    suspend fun invoke(login:String) : Flow<BaseResult<List<UserEntity>, Failure>> {
        return userRepository.getUserFollowers(login)
    }
}
