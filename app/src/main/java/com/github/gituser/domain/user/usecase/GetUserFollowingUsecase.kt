package com.github.gituser.domain.user.usecase


import android.util.Log
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFollowingUsecase @Inject constructor(private val userRepository: UserRepository){
    suspend fun invoke(login:String) : Flow<BaseResult<List<UserEntity>, Failure>> {
        return userRepository.getUserFollowing(login)
    }

    companion object {
        private const val TAG ="USECASE_TAG"
    }
}
