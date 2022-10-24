package com.teguh.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.MainDispatcherRule
import com.teguh.storyapp.data.remote.retrofit.ApiServiceAuth
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserAuthRepositoryTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiServiceAuth: ApiServiceAuth
    private lateinit var userAuthRepository: UserAuthRepository

    @Before
    fun setUp() {
        apiServiceAuth = FakeApiServiceAuth()
        userAuthRepository = UserAuthRepository(apiServiceAuth)
    }

    @Test
    fun `when register is Success`() = runTest {
        val expected = DataDummy.generateDummyRegisterResponse()
        val requestRegister = RequestRegister.setRequestRegister("tghswtn160","tghstwn@gmail.com", "admin12345")
        val actualData = userAuthRepository.register(requestRegister).apply {
                getOrAwaitValue() // konsumsi dahulu Result.LoadingrequestRegister
            }.getOrAwaitValue()

        assertNotNull(actualData)
        assertTrue(actualData is Result.Success)
        assertEquals(expected.error, (actualData as Result.Success).data.error)
    }

    @Test
    fun `when login is Success`() = runTest {
        val expected = DataDummy.generateDummyLoginResponse()
        val requestLogin = RequestLogin.setRequestLogin("tghstwn@gmail.com", "tghswtn160")
        val actualData = userAuthRepository.login(requestLogin).apply {
                getOrAwaitValue() // konsumsi dahulu Result.Loading
            }.getOrAwaitValue()

        assertNotNull(actualData)
        assertTrue(actualData is Result.Success)
        assertEquals(expected.message, (actualData as Result.Success).data.message)
    }
}