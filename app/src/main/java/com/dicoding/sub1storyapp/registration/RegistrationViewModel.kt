package com.dicoding.sub1storyapp.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.authpack.AuthBody
import com.dicoding.sub1storyapp.data.remote.authpack.AuthResponse
import com.dicoding.sub1storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    fun registUser(authBody: AuthBody): LiveData<ApiResponse<Response<AuthResponse>>> {
        val result = MutableLiveData<ApiResponse<Response<AuthResponse>>>()
        viewModelScope.launch {
            authRepository.registUser(authBody).collect() {
                result.postValue(it)
            }
        }
        return result
    }

}