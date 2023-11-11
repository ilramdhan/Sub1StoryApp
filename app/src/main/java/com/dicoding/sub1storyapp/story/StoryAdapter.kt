package com.dicoding.sub1storyapp.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sub1storyapp.data.model.Story
import com.dicoding.sub1storyapp.databinding.RowStoryBinding
import com.dicoding.sub1storyapp.utils.ValManager
import com.dicoding.sub1storyapp.utils.extension.setImageUrl
import com.dicoding.sub1storyapp.utils.extension.timeStamptoString

class StoryAdapter (private val storyList: List<Story>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.StoryViewHolder {
        val binding = RowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int = storyList.size

    override fun onBindViewHolder(holder: StoryAdapter.StoryViewHolder, position: Int) {
        storyList[position].let { story ->
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: RowStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                tvTitle.text = story.name
                tvDesc.text = story.description
                tvDate.text = story.createdAt.timeStamptoString()
                imgThumb.setImageUrl(story.photoUrl, true)
            }
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(ValManager.KEY_PACK_STORY, story)

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.imgThumb, "thumbnail"),
                    Pair(binding.tvTitle, "title"),
                    Pair(binding.tvDesc, "description"),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}