package com.github.gituser.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class GetUserFollowingUsecaseTest{
    private lateinit var getUserFollowingUsecase: GetUserFollowingUsecase
    @Mock
    private lateinit var userRepository: UserRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        getUserFollowingUsecase = GetUserFollowingUsecase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should emit list UserEntity when call is successfully`() = runTest {
        val query = "John Doe"
        val userEntity = listOf<UserEntity>(UserEntity(username = query, avatar = null))
        val baseResultSuccess = BaseResult.Success(userEntity)
        val flow = flow {
            emit(baseResultSuccess)
        }

        // arrange
        Mockito.`when`(userRepository.getUserFollowing(query)).thenReturn(flow)

        // act
        val result = getUserFollowingUsecase.invoke(query).first()

        // assert
        verify(userRepository).getUserFollowing(query)
        if (result is BaseResult.Success) assertEquals(userEntity, result.data)
    }

    @Test
    fun `should emit Failure when call is unsuccessfully`() = runTest {
        val query = "John Doe"
        val userEntity = listOf<UserEntity>(UserEntity(username = query, avatar = null))
        val baseResultError = BaseResult.Error(Failure(404, "NOT FOUND"))
        val flow = flow {
            emit(baseResultError)
        }

        // arrange
        Mockito.`when`(userRepository.getUserFollowing(query)).thenReturn(flow)

        // act
        val result = getUserFollowingUsecase.invoke(query).first()

        // assert
        verify(userRepository).getUserFollowing(query)
        if (result is BaseResult.Error) assertEquals(baseResultError, result)
    }
}