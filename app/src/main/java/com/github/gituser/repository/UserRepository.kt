package com.github.gituser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.gituser.Database.User
import com.github.gituser.Database.UserDao
import com.github.gituser.Database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUsersDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUsersDao = db.userDao()
    }
    fun getAllUsers(): LiveData<List<User>> = mUsersDao.getAllNotes()
    fun insert(user: User) {
        executorService.execute { mUsersDao.insert(user) }
    }
    fun delete(user: User) {
        executorService.execute { mUsersDao.delete(user) }
    }
    fun update(user: User) {
        executorService.execute { mUsersDao.update(user) }
    }
}