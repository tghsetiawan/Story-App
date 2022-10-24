package com.teguh.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.data.remote.response.ResponseRegister
import com.teguh.storyapp.data.repository.UserAuthRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.remote.response.ResponseLogin
import com.teguh.storyapp.getOrAwaitValue
import kotlinx.coroutines.test.runTest

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userAuthRepository: UserAuthRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyAuthRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyAuthRegisterRequest = DataDummy.generateDummyRegisterRequestBody()
    private val dummyAuthLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyAuthLoginRequest = DataDummy.generateDummyLoginRequest()

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(userAuthRepository)
    }

    @Test
    fun `when Register Should Not Null and Return Success`() = runTest {
        val expectedRegister = MutableLiveData<Result<ResponseRegister>>()
        expectedRegister.value = Result.Success(dummyAuthRegisterResponse)

        Mockito.`when`(userAuthRepository.register(dummyAuthRegisterRequest)).thenReturn(expectedRegister)

        val actual = authViewModel.register(dummyAuthRegisterRequest).getOrAwaitValue()
        Mockito.verify(userAuthRepository).register(dummyAuthRegisterRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Success)
    }

    @Test
    fun `when Register Network Error Should Return Error`() = runTest {
        val expectedRegister = MutableLiveData<Result<ResponseRegister>>()
        expectedRegister.value = Result.Error("Error")

        Mockito.`when`(userAuthRepository.register(dummyAuthRegisterRequest)).thenReturn(expectedRegister)

        val actual = authViewModel.register(dummyAuthRegisterRequest).getOrAwaitValue()
        Mockito.verify(userAuthRepository).register(dummyAuthRegisterRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Error)
    }

    @Test
    fun `when Login Should Not Null and Return Success`() = runTest {
        val expectedLogin = MutableLiveData<Result<ResponseLogin>>()
        expectedLogin.value = Result.Success(dummyAuthLoginResponse)

        Mockito.`when`(userAuthRepository.login(dummyAuthLoginRequest)).thenReturn(expectedLogin)

        val actual = authViewModel.login(dummyAuthLoginRequest).getOrAwaitValue()
        Mockito.verify(userAuthRepository).login(dummyAuthLoginRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Success)
    }

    @Test
    fun `when Login Network Error Should Return Error`() = runTest {
        val expectedLogin = MutableLiveData<Result<ResponseLogin>>()
        expectedLogin.value = Result.Error("Error")

        Mockito.`when`(userAuthRepository.login(dummyAuthLoginRequest)).thenReturn(expectedLogin)

        val actual = authViewModel.login(dummyAuthLoginRequest).getOrAwaitValue()
        Mockito.verify(userAuthRepository).login(dummyAuthLoginRequest)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Error)
    }

}

