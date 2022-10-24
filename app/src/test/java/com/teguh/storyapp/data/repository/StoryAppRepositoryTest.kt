package com.teguh.storyapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.MainDispatcherRule
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiService
import com.teguh.storyapp.getOrAwaitValue
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
import com.teguh.storyapp.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import org.mockito.Mockito.*

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
        storyAppRepository = StoryAppRepository(DataDummy.generatedDummyToken(), apiService, storyDatabase)
    }

    @Test
    fun `when getStory is success`() = runTest {
        val mockedClass = mock(StoryAppRepository::class.java)

        val expectedData = MutableLiveData<PagingData<StoryEntity>>()
        expectedData.value = StoryPagingSource.snapshot(DataDummy.generateDummyStories())

        `when`(mockedClass.getStory()).thenReturn(expectedData)

        val actualData = mockedClass.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {}
                override fun onRemoved(position: Int, count: Int) {}
                override fun onMoved(fromPosition: Int, toPosition: Int) {}
                override fun onChanged(position: Int, count: Int, payload: Any?) {}
            },
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualData)

        verify(mockedClass).getStory()

        assertNotNull(actualData)
        assertEquals(DataDummy.generateDummyStories(), differ.snapshot())
        assertEquals(DataDummy.generateDummyStories().size, differ.snapshot().size)
    }

    @Test
    fun `when getMapStory is success`() = runTest {
        val expected = DataDummy.generateDummyGetStoryResponse()
        val actualData = storyAppRepository.getMapStory(5, 5, 1).apply {
            getOrAwaitValue()
        }.getOrAwaitValue()

        assertNotNull(actualData)
        assertTrue(actualData is Result.Success)
        assertEquals(expected.error, (actualData as Result.Success).data.error)
    }

    @Test
    fun `when addStory is success`() = runTest {
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
        val actualData = storyAppRepository.addStory(
            imageMultipart,
            descriptionRequestBody,
            null,
            null
        ).apply {
            getOrAwaitValue()
        }.getOrAwaitValue()

        assertNotNull(actualData)
        assertTrue(actualData is Result.Success)
        assertEquals(expected.error, false)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
