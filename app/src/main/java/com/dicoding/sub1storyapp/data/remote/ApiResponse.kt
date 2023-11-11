package com.dicoding.sub1storyapp.data.remote

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
    data object Empty : ApiResponse<Nothing>()
}
