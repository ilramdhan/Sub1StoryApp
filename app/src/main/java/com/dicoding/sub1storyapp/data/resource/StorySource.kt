package com.dicoding.sub1storyapp.data.resource

import com.dicoding.sub1storyapp.data.local.StoryDao
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.story.AddStoryResponse
import com.dicoding.sub1storyapp.data.remote.story.Service
import com.dicoding.sub1storyapp.data.remote.story.StoryResponse
import com.dicoding.sub1storyapp.data.remote.story.toStoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorySource @Inject constructor(
    private val dao: StoryDao,
    private val service: Service
) {

    suspend fun getStory(token: String): Flow<ApiResponse<StoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.getStory(token)
                if (!response.error) {
                    dao.deleteAllStories()
                    val storyList = response.listStory.map {
                        toStoryEntity(it)
                    }
                    dao.insertStories(storyList)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = service.addStory(token, file, description)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }
}