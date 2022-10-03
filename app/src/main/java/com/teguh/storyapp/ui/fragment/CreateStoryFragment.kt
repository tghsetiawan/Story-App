package com.teguh.storyapp.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.teguh.storyapp.R
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.databinding.FragmentNewStoryBinding
import com.teguh.storyapp.databinding.FragmentStoryBottomSheetBinding
import com.teguh.storyapp.utils.*
import com.teguh.storyapp.utils.Param.Companion.TAG
import com.teguh.storyapp.viewmodel.StoryViewModel
import com.teguh.storyapp.viewmodel.StoryViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryFragment : Fragment()  {
    private var binding: FragmentNewStoryBinding? = null
    private var getFile: File? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var storyViewModel: StoryViewModel? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentPhotoPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewStoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factoryStory: StoryViewModelFactory = StoryViewModelFactory.getInstance(requireActivity())
        storyViewModel = ViewModelProvider(this, factoryStory)[StoryViewModel::class.java]

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkPermissionCamera()

        binding?.ivMore?.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val binding = FragmentStoryBottomSheetBinding.inflate(layoutInflater)

            binding.layoutAddImage.setOnClickListener {
                dialog.cancel()
                openGallery()
            }

            binding.layoutAddPhoto.setOnClickListener {
                dialog.cancel()
                openCamera()
            }

            dialog.setCancelable(true)
            dialog.setContentView(binding.root)
            dialog.show()
        }

        binding?.ivBack?.setOnClickListener {
            navigateUp(it)
        }

        binding?.switchShareMyLocation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                latitude = null
                longitude = null
            }
        }

        binding?.ivDone?.setOnClickListener {
            if (getFile != null) {
                val token = getPreference(requireContext(), Constant.USER_TOKEN)
                val file = reduceFileImage(getFile as File)
                val description = binding?.edtDescription?.text.toString()
                val requestImageFile =
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                if(description.isNotEmpty()) {
                    val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                    val latitudeRequestBody = if (latitude != null)
                        latitude.toString().toRequestBody("text/plain".toMediaType()) else null

                    val longitudeRequestBody = if (longitude != null)
                        longitude.toString().toRequestBody("text/plain".toMediaType()) else null

                    storyViewModel?.addStory(
                        token,
                        imageMultipart,
                        descriptionRequestBody,
                        latitudeRequestBody,
                        longitudeRequestBody)?.observe(viewLifecycleOwner) { res ->
                        if (res != null) {
                            when (res) {
                                is Result.Loading -> {
                                    this@CreateStoryFragment.showLoading()
                                }
                                is Result.Success -> {
                                    hideLoading()
                                    navigateUp(view)
                                }
                                is Result.Error -> {
                                    hideLoading()
                                    requireView().showSnackBar(res.error)
                                }
                            }
                        }
                    }
                }
            } else {
                requireContext().showToast("Please Upload Image First")
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity().application).also {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.teguh.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile

            binding?.ivStory?.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding?.ivStory?.setImageBitmap(result)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->
        Log.d(TAG, "$permissions")
        when (permissions) {
            true -> {
                getMyLocation()
            }
            else -> {
                Snackbar
                    .make(requireView(), getString(R.string.access_location_off), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.turn_on)) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    .show()
                binding?.switchShareMyLocation?.isChecked = false
            }
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d(TAG, "getMyLocation: $latitude, $longitude")
                } else {
                    binding?.switchShareMyLocation?.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun checkPermissionCamera() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    requireContext().showToast(getString(R.string.permission_granted))
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    requireContext().showToast(getString(R.string.permission_reject))
                    requireActivity().finish()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }

}