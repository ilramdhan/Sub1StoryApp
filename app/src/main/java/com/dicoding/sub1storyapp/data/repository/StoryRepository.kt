package com.dicoding.sub1storyapp.data.repository

import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.story.AddStoryResponse
import com.dicoding.sub1storyapp.data.remote.story.StoryResponse
import com.dicoding.sub1storyapp.data.resource.StorySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(private val storySource: StorySource) {

    suspend fun getStory(token: String): Flow<ApiResponse<StoryResponse>> {
        return storySource.getStory(token).flowOn(Dispatchers.IO)
    }

    suspend fun addStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return storySource.addNewStory(token, file, description)
    }

}