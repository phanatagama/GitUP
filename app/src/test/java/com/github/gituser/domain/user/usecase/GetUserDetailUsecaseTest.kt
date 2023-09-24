package com.github.gituser.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.gituser.domain.common.base.BaseResult
import com.github.gituser.domain.common.base.Failure
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class GetUserDetailUsecaseTest {
    private lateinit var getUserDetailUsecase: GetUserDetailUsecase
    @Mock
    private lateinit var userRepository: UserRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        getUserDetailUsecase = GetUserDetailUsecase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should emit userDetail when call is successfully`() = runTest {
        val username ="John Doe"
        val userDetailEntity = UserDetailEntity(username)
        val flow = flow { emit(BaseResult.Success(userDetailEntity)) }

        // arrange
        `when`(userRepository.getUserDetail(username)).thenReturn(flow)

        // act
        val result = getUserDetailUsecase.invoke(username).first()

        // assert
        verify(userRepository).getUserDetail(username)

        assertTrue(result is BaseResult.Success)
        if(result is BaseResult.Success) assertEquals(userDetailEntity, result.data)
    }

    @Test
    fun `should emit Failure when call is unsuccessfully`() = runTest {
        val username ="John Doe"
//        val userDetailEntity = UserDetailEntity(username)
        val err = Failure(404, "Not Found")
        val flow = flow { emit(BaseResult.Error(err)) }

        // arrange
        `when`(userRepository.getUserDetail(username)).thenReturn(flow)

        // act
        val result = getUserDetailUsecase.invoke(username).first()

        // assert
        verify(userRepository).getUserDetail(username)

        assertTrue(result is BaseResult.Error)
        if(result is BaseResult.Error) assertEquals(err, result.err)
    }
}