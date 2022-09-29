package com.teguh.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.databinding.ItemRvStoryBinding
import com.teguh.storyapp.ui.adapter.StoryAdapter.HomeViewHolder

class StoryAdapter : PagingDataAdapter<StoryEntity, HomeViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((StoryEntity, ImageView, TextView, TextView) -> Unit)? = null

    inner class HomeViewHolder(private val binding: ItemRvStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(listStory: StoryEntity) {
            binding.viewmodel = listStory
            binding.executePendingBindings()

            itemView.setOnClickListener {
                onItemClick?.invoke(listStory, binding.rivImgStory, binding.tvTitleName, binding.tvDesc)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.HomeViewHolder {
        return HomeViewHolder(ItemRvStoryBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: StoryAdapter.HomeViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}