package com.teguh.storyapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.teguh.storyapp.MainActivity
import com.teguh.storyapp.R
import com.teguh.storyapp.databinding.FragmentSettingBinding
import com.teguh.storyapp.utils.clearPreference
import com.teguh.storyapp.utils.navigateUp

class SettingFragment : Fragment() {
    private var binding: FragmentSettingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivBack?.setOnClickListener {
            navigateUp(it)
        }

        binding?.tvLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding?.tvLogout?.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_description))
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                    dialog.cancel()
                    clearPreference(requireContext())
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
                .show()
        }

    }


}