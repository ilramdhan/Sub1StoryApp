package com.dicoding.sub1storyapp.di

import android.content.Context
import androidx.room.Room
import com.dicoding.sub1storyapp.data.local.StoryDao
import com.dicoding.sub1storyapp.data.local.StoryDatabase
import com.dicoding.sub1storyapp.utils.ValManager.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Local {
    @Singleton
    @Provides

    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase {
    return Room.databaseBuilder(context, StoryDatabase::class.java, DB)
        .fallbackToDestructiveMigration()
        .build()
}
    @Provides
    fun provideStoryDao(database: StoryDatabase): StoryDao = database.getStoryDao()

}