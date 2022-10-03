package com.teguh.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.data.repository.StoryAppRepository
import com.teguh.storyapp.data.repository.UserAuthRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

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
    fun addStory() {
    }

    @Test
    fun getMapStories() {
    }

    @Test
    fun getStories() {
    }
}