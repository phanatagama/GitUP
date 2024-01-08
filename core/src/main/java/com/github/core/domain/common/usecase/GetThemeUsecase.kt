package com.github.core.domain.common.usecase

import com.github.core.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUsecase constructor(private val repository: UserRepository){
    fun invoke(): Flow<Boolean> = repository.getTheme()
}