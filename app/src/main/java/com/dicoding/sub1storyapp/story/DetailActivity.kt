package com.dicoding.sub1storyapp.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.data.model.Story
import com.dicoding.sub1storyapp.databinding.ActivityDetailBinding
import com.dicoding.sub1storyapp.utils.ValManager
import com.dicoding.sub1storyapp.utils.extension.setImageUrl
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private var _activityDetailStoryBinding: ActivityDetailBinding? = null
    private val binding get() = _activityDetailStoryBinding!!
    private lateinit var story: Story
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailStoryBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(_activityDetailStoryBinding?.root)

        initIntent()
        initUI()
    }
    private fun initIntent() {
        story = intent.getParcelableExtra(ValManager.KEY_PACK_STORY)!!
    }
    private fun initUI() {
        binding.apply {
            imgStory.setImageUrl(story.photoUrl, true)
            tvTitle.text = story.name
            tvDesc.text = story.description
        }
        title = getString(string.title_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}