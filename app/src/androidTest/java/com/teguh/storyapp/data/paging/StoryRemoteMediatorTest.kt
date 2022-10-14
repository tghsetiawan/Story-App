package com.teguh.storyapp.data.paging

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import com.teguh.storyapp.data.remote.response.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest{

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUhuTDBhdnZaeDVwNEdFV2UiLCJpYXQiOjE2NjUzODMzMjd9.8DJNKdUj2k7BtWNjcwvUuQQ9EhSJ6w248l0UtNrBI9Q"
        )
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }

}

class FakeApiService : ApiService {

    override suspend fun addStory(
        token: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ResponseAddStory {
        return ResponseAddStory(
            false,
            "success"
        )
    }

    override suspend fun getStory(
        token: String?,
        page: Int,
        size: Int,
        location: Int
    ): ResponseGetStory {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val stories = StoryEntity(
                "story-$i",
                "dicoding",
                "dicoding story desc",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-10-03T03:48:09.330Z",
                -6.288288288288288,
                106.82289451908555,
            )
            items.add(stories)
        }

        return ResponseGetStory(
            false,
            "Stories fetched successfully",
            items.subList((page - 1) * size, (page - 1) * size + size),
        )
    }
}