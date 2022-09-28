package com.teguh.storyapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teguh.storyapp.R
import com.teguh.storyapp.utils.Constant.USER_TOKEN
import com.teguh.storyapp.utils.Param.Companion.TAG
import com.teguh.storyapp.utils.getPreference

class SplashScreenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor = resources.getColor(R.color.colorBackground_1)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({ loginPage() }, 3000)
    }

    private fun loginPage() {
        val token = getPreference(requireContext(), USER_TOKEN)
        Log.e(TAG, "Token : $token")

        if(token.isNotEmpty()){
            findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment, null, NavOptions.Builder().setPopUpTo(R.id.signInFragment, true).build())
        } else {
            findNavController().navigate(R.id.action_splashScreenFragment_to_signInFragment, null, NavOptions.Builder().setPopUpTo(R.id.signInFragment, true).build())
        }
    }
}