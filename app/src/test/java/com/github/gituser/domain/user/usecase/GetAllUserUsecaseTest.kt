package com.github.gituser.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.repository.UserRepository
import com.github.core.domain.user.usecase.GetAllUserUsecase
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class GetAllUserUsecaseTest {
    private lateinit var getAllUserUsecase: GetAllUserUsecase

    @Mock
    private lateinit var userRepository: UserRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        getAllUserUsecase = GetAllUserUsecase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should emit list userDetail`() = runTest {
        val userDetail = listOf(UserDetail("joko"))
        val userFlow = flow { emit(userDetail) }

        // arrange
        `when`(userRepository.getAllUser()).thenReturn(userFlow)

        // act
        val result = getAllUserUsecase.invoke().first()

        // assert
        verify(userRepository).getAllUser()
        assertEquals(userDetail, result)
    }

}