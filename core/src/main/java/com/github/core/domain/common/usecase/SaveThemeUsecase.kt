package com.github.core.domain.common.usecase

import com.github.core.domain.user.repository.UserRepository

class SaveThemeUsecase constructor(private val repository: UserRepository) {
    suspend fun invoke(isDarkModeActive:Boolean) = repository.saveTheme(isDarkModeActive)
}