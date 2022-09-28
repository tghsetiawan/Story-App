package com.teguh.storyapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.databinding.FragmentStoryDetailBinding
import com.teguh.storyapp.utils.navigateUp

class StoryDetailFragment: Fragment() {
    private var binding: FragmentStoryDetailBinding? = null
    private val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_right)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStoryDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateView(args.data)

        binding?.ivBack?.setOnClickListener {
            navigateUp(it)
        }
    }

    private fun populateView(data: StoryEntity) {
        binding?.apply {
            viewmodel = data
            executePendingBindings()
        }
    }

}