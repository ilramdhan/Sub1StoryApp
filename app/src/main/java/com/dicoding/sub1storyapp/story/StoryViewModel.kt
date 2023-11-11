package com.dicoding.sub1storyapp.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.story.AddStoryResponse
import com.dicoding.sub1storyapp.data.remote.story.StoryResponse
import com.dicoding.sub1storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(private val storyRepository: StoryRepository): ViewModel() {

    fun getStory(token: String) : LiveData<ApiResponse<StoryResponse>> {
        val result = MutableLiveData<ApiResponse<StoryResponse>>()
        viewModelScope.launch {
            storyRepository.getStory(token).collect {
                result.postValue(it)
            }
        }
        return result
    }

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<ApiResponse<AddStoryResponse>> {
        val result = MutableLiveData<ApiResponse<AddStoryResponse>>()
        viewModelScope.launch {
            storyRepository.addStory(token, file, description).collect {
                result.postValue(it)
            }
        }
        return result
    }

}