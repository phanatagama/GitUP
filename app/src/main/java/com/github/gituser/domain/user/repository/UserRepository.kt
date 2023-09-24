package com.github.gituser.domain.user.repository

import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    // remote
    suspend fun getUsersByQuery(q:String) : Flow<BaseResult<List<UserEntity>, Failure>>
    suspend fun getUserFollowing(login:String) : Flow<BaseResult<List<UserEntity>, Failure>>
    suspend fun getUserFollowers(login:String) : Flow<BaseResult<List<UserEntity>, Failure>>
    suspend fun getUserDetail(login:String) : Flow<BaseResult<UserDetailEntity, Failure>>

    // local
    fun getAllUser() : Flow<List<UserDetailEntity>>
    suspend fun insertUser(userDetailEntity: UserDetailEntity) : BaseResult<Unit, Failure>
    suspend fun deleteUser(userDetailEntity: UserDetailEntity) : BaseResult<Unit, Failure>
}
//class UserRepository(application: Application) {
//    private val mUsersDao: UserDao
//    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
//
//    init {
//        val db = UserRoomDatabase.getDatabase(application)
//        mUsersDao = db.userDao()
//    }
//    fun getAllUsers(): LiveData<List<UserDetailEntity>> = mUsersDao.getAllUsers()
//    fun insert(user: UserDetailEntity) {
//        executorService.execute { mUsersDao.insert(user) }
//    }
//    fun delete(user: UserDetailEntity) {
//        executorService.execute { mUsersDao.delete(user) }
//    }
//}