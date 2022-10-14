package com.teguh.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.MainDispatcherRule
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.remote.response.ResponseAddStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.repository.StoryAppRepository
import com.teguh.storyapp.getOrAwaitValue
import com.teguh.storyapp.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyAppRepository)
    }

    @Test
    fun `when addStory Should Not Null and Return Success`() = runTest  {
        val file = File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1496146656900.jpg")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
        val descriptionRequestBody = "description testing".toRequestBody("text/plain".toMediaType())
        val expectedStories = MutableLiveData<Result<ResponseAddStory>>()
        expectedStories.value = Result.Error("Error")

        Mockito.`when`(
            storyAppRepository.addStory(
                imageMultipart,
                descriptionRequestBody,
                null,
                null
            )
        ).thenReturn(expectedStories)

        val actual = storyViewModel.addStory(imageMultipart, descriptionRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyAppRepository).addStory(imageMultipart, descriptionRequestBody, null, null)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Error)
    }

    @Test
    fun `when addStory Network Error Should Return Error`() = runTest  {
        val file = File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1496146656900.jpg")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
        val descriptionRequestBody = "description testing".toRequestBody("text/plain".toMediaType())
        val expectedStories = MutableLiveData<Result<ResponseAddStory>>()
        expectedStories.value = Result.Success(DataDummy.generateDummyAddStoryResponse())

        Mockito.`when`(
            storyAppRepository.addStory(
                imageMultipart,
                descriptionRequestBody,
                null,
                null
            )
        ).thenReturn(expectedStories)

        val actual = storyViewModel.addStory(imageMultipart, descriptionRequestBody, null, null).getOrAwaitValue()
        Mockito.verify(storyAppRepository).addStory(imageMultipart, descriptionRequestBody, null, null)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Success)
    }

    @Test
    fun `when getMapStories Should Not Null and Return Success`() = runTest  {
        val expectedStories = MutableLiveData<Result<ResponseGetStory>>()
        expectedStories.value = Result.Success(dummyStory)

        Mockito.`when`(storyAppRepository.getMapStory(location = 1, page = 1, size = 5)).thenReturn(expectedStories)

        val actual = storyViewModel.getMapStories(location = 1, page = 1, size = 5).getOrAwaitValue()
        Mockito.verify(storyAppRepository).getMapStory(location = 1, page = 1, size = 5)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Success)
        Assert.assertEquals(dummyStory.listStory.size, (actual as Result.Success).data.listStory.size)
    }

    @Test
    fun `when getMapStories Network Error Should Return Error`() = runTest {
        val expectedStories = MutableLiveData<Result<ResponseGetStory>>()
        expectedStories.value = Result.Error("Error")

        Mockito.`when`(storyAppRepository.getMapStory(location = 1, page = 1, size = 5)).thenReturn(expectedStories)

        val actual = storyViewModel.getMapStories(location = 1, page = 1, size = 5).getOrAwaitValue()
        Mockito.verify(storyAppRepository).getMapStory(location = 1, page = 1, size = 5)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is Result.Error)
    }

    @Test
    fun `when getStories Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStories()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStories = MutableLiveData<PagingData<StoryEntity>>()
        expectedStories.value = data
        Mockito.`when`(storyAppRepository.getStory()).thenReturn(expectedStories)

        val mainViewModel = StoryViewModel(storyAppRepository)
        val actual: PagingData<StoryEntity> = mainViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actual)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
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

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}