package com.teguh.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.teguh.storyapp.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.repository.StoryAppRepository
import com.teguh.storyapp.getOrAwaitValue

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository
    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyAppRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<Result<ResponseGetStory>>()
        expectedStories.value = Result.Success(dummyStory)

        Mockito.`when`(storyAppRepository.getMapStory(location = 1, page = 1, size = 5)).thenReturn(expectedStories)

        val actualNews = storyViewModel.getMapStories(location = 1, page = 1, size = 5).getOrAwaitValue()
        Mockito.verify(storyAppRepository).getMapStory(location = 1, page = 1, size = 5)
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Success)
        Assert.assertEquals(dummyStory.listStory.size, (actualNews as Result.Success).data.listStory.size)
    }

    @Test
    fun `when Get Stories Network Error Should Return Error`() {
        val expectedStories = MutableLiveData<Result<ResponseGetStory>>()
        expectedStories.value = Result.Error("Error")

        Mockito.`when`(storyAppRepository.getMapStory(location = 1, page = 1, size = 5)).thenReturn(expectedStories)

        val actualNews = storyViewModel.getMapStories(location = 1, page = 1, size = 5).getOrAwaitValue()
        Mockito.verify(storyAppRepository).getMapStory(location = 1, page = 1, size = 5)
        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Error)
    }
}