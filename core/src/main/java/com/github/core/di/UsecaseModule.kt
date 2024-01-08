package com.github.core.di

import com.github.core.domain.common.usecase.GetThemeUsecase
import com.github.core.domain.common.usecase.SaveThemeUsecase
import com.github.core.domain.user.usecase.DeleteUserUsecase
import com.github.core.domain.user.usecase.GetAllUserUsecase
import com.github.core.domain.user.usecase.GetUserDetailUsecase
import com.github.core.domain.user.usecase.GetUserFollowersUsecase
import com.github.core.domain.user.usecase.GetUserFollowingUsecase
import com.github.core.domain.user.usecase.GetUsersByQueryUsecase
import com.github.core.domain.user.usecase.InsertUserUsecase
import org.koin.dsl.module

val usecaseModule = module {
    factory { DeleteUserUsecase(get()) }
    factory { GetAllUserUsecase(get()) }
    factory { GetUsersByQueryUsecase(get()) }
    factory { GetUserDetailUsecase(get()) }
    factory { GetUserFollowersUsecase(get()) }
    factory { GetUserFollowingUsecase(get()) }
    factory { InsertUserUsecase(get())}
    factory { GetThemeUsecase(get()) }
    factory { SaveThemeUsecase(get()) }
}