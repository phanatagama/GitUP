package com.github.gituser.domain.user.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.core.domain.common.base.BaseResult
import com.github.core.domain.common.base.Failure
import com.github.core.domain.user.model.UserDetail
import com.github.core.domain.user.repository.UserRepository
import com.github.core.domain.user.usecase.DeleteUserUsecase
import kotlinx.coroutines.Dispatchers
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DeleteUserUsecaseTest{
    private lateinit var deleteUserUsecase: DeleteUserUsecase
    @Mock
    private lateinit var userRepository: UserRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        deleteUserUsecase = DeleteUserUsecase(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should emit Unit when call is successfully` () = runTest {
        val username ="John Doe"
        val userDetail = UserDetail(username)
        val baseResultSuccess = BaseResult.Success(Unit)

        // arrange
        `when`(userRepository.deleteUser(userDetail)).thenReturn(baseResultSuccess)

        // act
        val result = deleteUserUsecase.invoke(userDetail)

        // assert
        verify(userRepository).deleteUser(userDetail)
        assertEquals(baseResultSuccess, result)
    }

    @Test
    fun `should emit Failure when call is unsuccessfully` () = runTest {
        val username ="John Doe"
        val userDetail = UserDetail(username)
        val baseResultError = BaseResult.Error(
            Failure(
                -1,
                "DatabaseException"
            )
        )

        // arrange
        `when`(userRepository.deleteUser(userDetail)).thenReturn(baseResultError)

        // act
        val result = deleteUserUsecase.invoke(userDetail)

        // assert
        verify(userRepository).deleteUser(userDetail)
        assertEquals(baseResultError, result)
    }
}