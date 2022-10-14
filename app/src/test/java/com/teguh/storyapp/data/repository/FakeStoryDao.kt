package com.teguh.storyapp.data.repository

import androidx.paging.PagingSource
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.local.room.StoryDao

class FakeStoryDao : StoryDao {
    override suspend fun insertStory(story: List<StoryEntity>) {
        TODO("Not yet implemented")
    }

    override fun getAllStory(): PagingSource<Int, StoryEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}