package com.teguh.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.teguh.storyapp.data.remote.request.RequestRegister

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userAuthRepository: UserAuthRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyAuthRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyAuthRegisterRequest = DataDummy.generateDummyRegisterRequestBody()

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(userAuthRepository)
    }

    @Test
    fun `when Register Should Not Null and Return Success`() {
        val observer = Observer<Result<ResponseRegister>> {}
        try {
            val expectedRegister = MutableLiveData<Result<ResponseRegister>>()
            expectedRegister.value = Result.Success(dummyAuthRegisterResponse)
            Mockito.`when`(userAuthRepository.register(dummyAuthRegisterRequest)).thenReturn(expectedRegister)

            val actualNews = authViewModel.register(dummyAuthRegisterRequest).observeForever(observer)

            Mockito.verify(userAuthRepository).register(dummyAuthRegisterRequest)
            Assert.assertNotNull(actualNews)
        } finally {
            authViewModel.register(dummyAuthRegisterRequest).removeObserver(observer)
        }
    }

    @Test
    fun `when Register Network Error Should Return Error`() {
        val observer = Observer<Result<ResponseRegister>> {}
        try {
            val expectedRegister = MutableLiveData<Result<ResponseRegister>>()
            expectedRegister.value = Result.Error("Error")
            Mockito.`when`(userAuthRepository.register(dummyAuthRegisterRequest)).thenReturn(expectedRegister)

            val actualNews = authViewModel.register(dummyAuthRegisterRequest).observeForever(observer)

            Mockito.verify(userAuthRepository).register(dummyAuthRegisterRequest)
            Assert.assertNotNull(actualNews)
        } finally {
            authViewModel.register(dummyAuthRegisterRequest).removeObserver(observer)
        }
    }

}

