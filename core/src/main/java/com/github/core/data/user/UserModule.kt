package com.github.core.data.user

import androidx.room.Room
import com.github.core.data.common.SettingPreferences
import com.github.core.data.user.local.UserLocalDataSource
import com.github.core.data.user.local.database.UserDao
import com.github.core.data.user.local.database.UserRoomDatabase
import com.github.core.data.user.remote.UserRemoteDataSource
import com.github.core.data.user.remote.api.UserApi
import com.github.core.domain.user.repository.UserRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val databaseModule = module {
    val passphrase: ByteArray = SQLiteDatabase.getBytes("user".toCharArray())
    val factory = SupportFactory(passphrase)
    single {
        Room.databaseBuilder(
            androidContext(),
            UserRoomDatabase::class.java, "user_database.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
    single { provideUserApi(get()) }
    single { provideUserRemoteDataSource(get()) }
    single { provideUserLocalDataSource(get()) }
    factory { provideUserRepository(get(), get(), get()) }
    factory { provideUserDao(get()) }
}

fun provideUserApi(retrofit: Retrofit): UserApi {
    return retrofit.create(UserApi::class.java)
}

fun provideUserRemoteDataSource(userApi: UserApi): UserRemoteDataSource {
    return UserRemoteDataSource(userApi)
}

fun provideUserDao(userRoomDatabase: UserRoomDatabase): UserDao {
    return userRoomDatabase.userDao()
}

fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource {
    return UserLocalDataSource(userDao)
}

fun provideUserRepository(
    userRemoteDataSource: UserRemoteDataSource,
    userLocalDataSource: UserLocalDataSource,
    preferences: SettingPreferences
): UserRepository {
    return UserRepositoryImpl(userRemoteDataSource, userLocalDataSource, preferences)
}