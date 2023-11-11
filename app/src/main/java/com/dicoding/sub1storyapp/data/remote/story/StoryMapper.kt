package com.dicoding.sub1storyapp.data.remote.story

import com.dicoding.sub1storyapp.data.local.StoryEntity
import com.dicoding.sub1storyapp.data.model.Story

fun toStoryEntity(story: Story): StoryEntity {
    return StoryEntity(
        id = story.id,
        photoUrl = story.photoUrl
    )
}