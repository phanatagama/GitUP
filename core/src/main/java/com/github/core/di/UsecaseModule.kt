package com.github.core.di

import com.github.core.domain.common.usecase.GetThemeUseCase
import com.github.core.domain.common.usecase.SaveThemeUseCase
import com.github.core.domain.user.usecase.DeleteUserUseCase
import com.github.core.domain.user.usecase.GetAllUserUseCase
import com.github.core.domain.user.usecase.GetUserDetailUseCase
import com.github.core.domain.user.usecase.GetUserFollowersUseCase
import com.github.core.domain.user.usecase.GetUserFollowingUseCase
import com.github.core.domain.user.usecase.GetUsersByQueryUseCase
import com.github.core.domain.user.usecase.InsertUserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { DeleteUserUseCase(get()) }
    factory { GetAllUserUseCase(get()) }
    factory { GetUsersByQueryUseCase(get()) }
    factory { GetUserDetailUseCase(get()) }
    factory { GetUserFollowersUseCase(get()) }
    factory { GetUserFollowingUseCase(get()) }
    factory { InsertUserUseCase(get())}
    factory { GetThemeUseCase(get()) }
    factory { SaveThemeUseCase(get()) }
}