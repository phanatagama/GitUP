package com.github.gituser.data.user

import android.content.Context
import com.github.gituser.data.common.NetworkModule
import com.github.gituser.data.user.local.UserLocalDataSource
import com.github.gituser.data.user.local.database.UserDao
import com.github.gituser.data.user.local.database.UserRoomDatabase
import com.github.gituser.data.user.remote.UserRemoteDataSource
import com.github.gituser.data.user.remote.api.UserApi
import com.github.gituser.domain.user.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object UserModule {
    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userApi: UserApi): UserRemoteDataSource{
        return UserRemoteDataSource(userApi)
    }

    @Singleton
    @Provides
    fun provideUserRoomDatabase(@ApplicationContext context: Context): UserRoomDatabase {
        return UserRoomDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideUserDao(userRoomDatabase: UserRoomDatabase): UserDao{
        return userRoomDatabase.userDao()
    }


    @Singleton
    @Provides
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource{
        return UserLocalDataSource(userDao)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userRemoteDataSource: UserRemoteDataSource, userLocalDataSource: UserLocalDataSource) : UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, userLocalDataSource)
    }
}