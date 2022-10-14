package com.teguh.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.MainDispatcherRule
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiService
import com.teguh.storyapp.data.remote.retrofit.ApiServiceAuth
import com.teguh.storyapp.getOrAwaitValue
import com.teguh.storyapp.ui.adapter.StoryAdapter
import com.teguh.storyapp.viewmodel.StoryPagingSource
import com.teguh.storyapp.viewmodel.StoryViewModel
import com.teguh.storyapp.viewmodel.noopListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
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
        val actual = apiServiceAuth.register(DataDummy.generateDummyRegisterRequestBody())
        assertNotNull(actual)
        assertEquals(expected.message, actual.message)
    }

    @Test
    fun `when login is Success`() = runTest {
        val expected = DataDummy.generateDummyLoginResponse()
        val actual = apiServiceAuth.login(DataDummy.generateDummyLoginRequest())
        assertNotNull(actual)
        assertEquals(expected.message, actual.message)
    }

}