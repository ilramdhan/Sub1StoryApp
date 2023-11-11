package com.dicoding.sub1storyapp.data.remote.authpack

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
