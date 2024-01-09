package com.github.gituser.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.User
import com.github.core.domain.user.repository.UserRepository
import com.github.core.domain.user.usecase.GetUserFollowersUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class GetUserFollowersUseCaseTest{
    private lateinit var getUserFollowersUseCase: GetUserFollowersUseCase
    @Mock
    private lateinit var userRepository: UserRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        getUserFollowersUseCase = GetUserFollowersUseCase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should emit list UserEntity when call is successfully`() = runTest {
        val query = "John Doe"
        val userEntity = listOf(User(username = query, avatar = null))
        val baseResultSuccess = BaseResult.Success(userEntity)
        val flow = flow {
            emit(baseResultSuccess)
        }

        // arrange
        Mockito.`when`(userRepository.getUserFollowers(query)).thenReturn(flow)

        // act
        val result = getUserFollowersUseCase.invoke(query).first()

        // assert
        verify(userRepository).getUserFollowers(query)
        if (result is BaseResult.Success) assertEquals(userEntity, result.data)
    }

    @Test
    fun `should emit Failure when call is unsuccessfully`() = runTest {
        val query = "John Doe"
        val baseResultError = BaseResult.Error(
            Failure(
                404,
                "NOT FOUND"
            )
        )
        val flow = flow {
            emit(baseResultError)
        }

        // arrange
        Mockito.`when`(userRepository.getUserFollowers(query)).thenReturn(flow)

        // act
        val result = getUserFollowersUseCase.invoke(query).first()

        // assert
        verify(userRepository).getUserFollowers(query)
        if (result is BaseResult.Error) assertEquals(baseResultError, result)
    }
}