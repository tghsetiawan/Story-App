package com.teguh.storyapp.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.teguh.storyapp.R
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.databinding.FragmentMapsBinding
import com.teguh.storyapp.utils.*
import com.teguh.storyapp.utils.Param.Companion.TAG
import com.teguh.storyapp.viewmodel.StoryViewModel
import com.teguh.storyapp.viewmodel.StoryViewModelFactory

class MapsFragment : Fragment() {
    private var binding: FragmentMapsBinding? = null
    private lateinit var mMap: GoogleMap
    private var storyViewModel: StoryViewModel? = null
    private var token: String? = null
    private lateinit var listImage: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = getPreference(requireContext(), Constant.USER_TOKEN)
        val factoryStory: StoryViewModelFactory = StoryViewModelFactory.getInstance(requireActivity())
        storyViewModel = ViewModelProvider(this, factoryStory)[StoryViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnPoiClickListener { point ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(point.latLng)
                    .title(point.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            poiMarker?.showInfoWindow()
        }

        loadStory()
        getMyLocation()
        setCustomMapStyle()
    }

    private fun loadStory(){
        storyViewModel?.getMapStories(page = 1, size = 10, location = 1)?.observe(viewLifecycleOwner) { res ->
            if (res != null) {
                when (res) {
                    is Result.Loading -> {
                        this@MapsFragment.showLoading()
                    }
                    is Result.Success -> {
                        Log.e(TAG, "Success Map Stories : ${res.data.message} ")
                        /*
                         * Input list image to show in stack widget
                        * */
                        listImage = ArrayList()
                        for (i in res.data.listStory) {
                            listImage.addAll(listOf(i.photoUrl))
                        }
                        putListPreference(
                            requireContext(),
                            Constant.LIST_STRING,
                            listImage
                        )

                        hideLoading()
                        val listStory = res.data.listStory
                        if (listStory != null) {
                            for (i in listStory) {
                                val listLatLng = LatLng(i.lat ?: 0.0,  i.lon ?: 0.0)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(listLatLng)
                                        .title(i.name)
                                        .snippet(i.description)
                                )
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listLatLng, 15F))
                            }
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error Map Stories : ${res.error} ")
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun setCustomMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            getMyLocation()
        }
    }
}