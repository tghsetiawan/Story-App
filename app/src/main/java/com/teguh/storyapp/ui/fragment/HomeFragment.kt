package com.teguh.storyapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.teguh.storyapp.R
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.databinding.FragmentHomeBinding
import com.teguh.storyapp.ui.adapter.LoadingStateAdapter
import com.teguh.storyapp.ui.adapter.StoryAdapter
import com.teguh.storyapp.utils.*
import com.teguh.storyapp.viewmodel.StoryViewModel
import com.teguh.storyapp.viewmodel.StoryViewModelFactory

class HomeFragment : Fragment()  {
    private var binding: FragmentHomeBinding? = null
    private var storyViewModel: StoryViewModel? = null
    private lateinit var storyAdapter: StoryAdapter
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.apply {
                    setTitle(R.string.exit_app)
                    setMessage(R.string.text_exit_app)
                    setPositiveButton(context.getString(R.string.yes)) { dialog, which ->
                        activity?.finish()
                    }
                    setNegativeButton(context.getString(R.string.no)) { dialog, which ->
                        dialog.dismiss()
                    }
                }
                val dialogAlert = alertDialog.create()
                if (dialogAlert.window != null) {
                    dialogAlert.window?.attributes?.windowAnimations = R.style.DialogTestAnimation
                }
                alertDialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = getPreference(requireContext(), Constant.USER_TOKEN)

        binding?.reyclerView?.setHasFixedSize(true)
        binding?.reyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val factoryStory: StoryViewModelFactory = StoryViewModelFactory.getInstance(requireActivity())
        storyViewModel = ViewModelProvider(this, factoryStory)[StoryViewModel::class.java]
        storyAdapter = StoryAdapter()

        binding?.reyclerView?.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        this@HomeFragment.showLoading()
        storyViewModel?.getStories(token!!)?.observe(viewLifecycleOwner) { res ->
            if(res != null){
                storyAdapter.submitData(lifecycle, res)
                binding?.reyclerView?.visible()
                binding?.lottieError?.gone()
                binding?.tvError?.gone()
            } else {
                binding?.reyclerView?.gone()
                binding?.lottieError?.visible()
                binding?.tvError?.visible()
            }
            hideLoading()
        }

        activity?.window?.statusBarColor = resources.getColor(R.color.colorBackground_1)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding?.fabCreateStory?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateStoryFragment())
        }

        binding?.ivSetting?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingFragment())
        }

        // Detail Story
        storyAdapter.onItemClick = { dataStory: StoryEntity, imgStory: ImageView, tvNameStory: TextView, tvDescriptionStory: TextView ->
            val extras = FragmentNavigatorExtras(
                imgStory to "storyImg",
                tvNameStory to "storyName",
                tvDescriptionStory to "storyDescription"
            )
            val action =
                HomeFragmentDirections.actionHomeFragmentToStoryDetailFragment(dataStory)
            findNavController().navigate(action, extras)
        }
    }
}