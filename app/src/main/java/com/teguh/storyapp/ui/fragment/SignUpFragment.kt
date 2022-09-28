package com.teguh.storyapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.teguh.storyapp.R
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.databinding.FragmentSignUpBinding
import com.teguh.storyapp.utils.*
import com.teguh.storyapp.utils.Param.Companion.TAG
import com.teguh.storyapp.viewmodel.AuthViewModel
import com.teguh.storyapp.viewmodel.AuthViewModelFactory


class SignUpFragment : Fragment()  {
    private var binding: FragmentSignUpBinding? = null
    private var authViewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val alertDialog = AlertDialog.Builder(context!!)
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
        //initialize databinding
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding?.root
    }

    private fun validationInputUser() {
        binding?.apply {
            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setEnabledButton() {
        binding?.apply {
            btnRegister.isEnabled = edtEmail.error == null && edtPassword.error == null &&
                    edtName.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() &&
                    edtPassword.text.toString().isNotEmpty()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validationInputUser()

        val factoryAuth: AuthViewModelFactory = AuthViewModelFactory.getInstance(requireActivity())
        authViewModel = ViewModelProvider(this, factoryAuth)[AuthViewModel::class.java]

        activity?.window?.statusBarColor = resources.getColor(R.color.colorBackground_1)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // pergi ke halaman sign in
        binding?.signin?.setOnClickListener {
            findNavController().navigate(
                R.id.action_signUpFragment_to_signInFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.signUpFragment, true).build()
            )
        }

        // button sign up
        binding?.btnRegister?.setOnClickListener {
            val name: String = binding?.edtName?.text.toString()
            val email: String = binding?.edtEmail?.text.toString()
            val password: String = binding?.edtPassword?.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(activity, "All Fields are Required!", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel?.register(RequestRegister.setRequestRegister(name, email, password))?.observe(viewLifecycleOwner) { res ->
                    if (res != null) {
                        when (res) {
                            is Result.Loading -> {
                                this@SignUpFragment.showLoading()
                            }
                            is Result.Success -> {
                                Log.e(TAG, "Success : ${res.data.message} ")
//                                hideLoading()
                                login(email, password)
                            }
                            is Result.Error -> {
                                Log.e(TAG, "Error : ${res.error} ")
                                hideLoading()
                                requireView().showSnackBar(res.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun login(email: String, password: String){
        authViewModel?.login(RequestLogin.setRequestLogin(email, password))?.observe(viewLifecycleOwner) { res ->
            if (res != null) {
                when (res) {
                    is Result.Loading -> {
                        this@SignUpFragment.showLoading()
                    }
                    is Result.Success -> {
                        Log.e(TAG, "Success Login : ${res.data.message} ")
                        hideLoading()
                        res.data.loginResult?.apply {
                            putPreference(requireContext(), Constant.USER_ID, userId!!)
                            putPreference(requireContext(), Constant.USER_NAME, name!!)
                            putPreference(requireContext(),
                                Constant.USER_TOKEN, getString(R.string.token, token))

                            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment, null, NavOptions.Builder().setPopUpTo(R.id.signInFragment, true).build())
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error Login : ${res.error} ")
                        hideLoading()
                        requireView().showSnackBar(res.error)
                    }
                }
            }
        }
    }
}