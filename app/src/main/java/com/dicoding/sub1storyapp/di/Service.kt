package com.dicoding.sub1storyapp.di

import com.dicoding.sub1storyapp.data.remote.authpack.AuthService
import com.dicoding.sub1storyapp.data.remote.story.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class Service {
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    fun provideStoryService(retrofit: Retrofit): Service = retrofit.create(Service::class.java)

}