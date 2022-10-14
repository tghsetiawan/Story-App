package com.teguh.storyapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.MainDispatcherRule
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiService
import com.teguh.storyapp.data.remote.retrofit.ApiServiceAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.File

@ExperimentalCoroutinesApi
class StoryAppRepositoryTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyAppRepository: StoryAppRepository
    private val context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storyDatabase = Room.inMemoryDatabaseBuilder(context, StoryDatabase::class.java).build()
        storyAppRepository = StoryAppRepository("token", apiService, storyDatabase)
    }

    @Test
    fun `when getStory success`() = runTest {
        val expectedStory = DataDummy.generateDummyGetStoryResponse()
        val actualStory = apiService.getStory(DataDummy.generatedDummyToken(), 5, 5, 0)

        assertNotNull(actualStory)
        assertEquals(expectedStory.listStory.size, actualStory.listStory.size)
    }

    @Test
    fun `when getMapStory success`() = runTest {
        val expectedStory = DataDummy.generateDummyGetStoryResponse()
        val actualStory = apiService.getStory(DataDummy.generatedDummyToken(), 5, 5, 1)

        assertNotNull(actualStory)
        assertEquals(expectedStory.listStory.size, actualStory.listStory.size)
    }

    @Test
    fun `when addStory success`() = runTest {
        val file = File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1496146656900.jpg")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
        val descriptionRequestBody = "description testing".toRequestBody("text/plain".toMediaType())

        val expected = DataDummy.generateDummyAddStoryResponse()
        val actual = apiService.addStory(
            DataDummy.generatedDummyToken(),
            imageMultipart,
            descriptionRequestBody,
            null,
            null
        )

        assertNotNull(expected)
        assertEquals(expected.message, actual.message)
    }
}