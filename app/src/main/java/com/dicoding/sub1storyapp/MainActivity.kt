package com.dicoding.sub1storyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.databinding.ActivityMainBinding
import com.dicoding.sub1storyapp.story.AddStoryActivity
import com.dicoding.sub1storyapp.story.StoryAdapter
import com.dicoding.sub1storyapp.story.StoryViewModel
import com.dicoding.sub1storyapp.user.UserActivity
import com.dicoding.sub1storyapp.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val storyVm: StoryViewModel by viewModels()
    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!
    private lateinit var pref: Session
    private var token: String? = null

    companion object {

        fun begin(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_activityMainBinding?.root)
        pref = Session(this)
        token = pref.getToken

        initAct()
        initUI()

        getStory("Bearer $token")
    }

    private fun initAct() {
        binding.addStory.setOnClickListener {
            AddStoryActivity.begin(this)
        }
        binding.btnProfile.setOnClickListener {
            UserActivity.begin(this)
        }
    }

    private fun initUI() {
        binding.rvItem.layoutManager = LinearLayoutManager(this)
        binding.tvName.text = getString(string.welcome_message, pref.getUserName)
    }

    private fun getStory(token: String) {
        storyVm.getStory(token).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> isLoading(true)
                is ApiResponse.Success -> {
                    isLoading(false)
                    val adapter = StoryAdapter(response.data.listStory)
                    binding.rvItem.adapter = adapter
                }
                is ApiResponse.Error -> isLoading(false)
                else -> {
                    Timber.e(getString(string.statement_error))
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.apply {
                flShimmer.visibility = View.VISIBLE
                flShimmer.startShimmer()
                rvItem.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                rvItem.visibility = View.VISIBLE
                flShimmer.stopShimmer()
                flShimmer.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.Setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getStory("Bearer $token")
    }
}