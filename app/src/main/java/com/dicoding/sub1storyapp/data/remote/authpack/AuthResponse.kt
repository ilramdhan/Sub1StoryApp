package com.dicoding.sub1storyapp.data.remote.authpack

import com.dicoding.sub1storyapp.data.model.User
import com.google.gson.annotations.SerializedName


data class AuthResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: User
)
