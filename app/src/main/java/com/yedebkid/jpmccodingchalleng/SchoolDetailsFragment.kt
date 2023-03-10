package com.yedebkid.jpmccodingchalleng

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yedebkid.jpmccodingchalleng.databinding.FragmentSchoolDetailsBinding
import com.yedebkid.jpmccodingchalleng.model.domain.School
import com.yedebkid.jpmccodingchalleng.model.domain.Score
import com.yedebkid.jpmccodingchalleng.util.BaseFragment
import com.yedebkid.jpmccodingchalleng.util.UIState

private const val TAG = "SchoolDetailsFragment"

class SchoolDetailsFragment : BaseFragment(), OnMapsSdkInitializedCallback {

    private val binding by lazy {
        FragmentSchoolDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        schoolViewModel.scores.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.LOADING -> {

                }
                is UIState.SUCCESS<*> -> {
                    val score = state.data as? Score
                    populateSchoolSatScores(score)
                }
                is UIState.ERROR -> {

                }
            }
        }

        populateSchoolDetails(schoolViewModel.school)

        return binding.root
    }

    private fun populateSchoolDetails(school: School? = null) {
        school?.let {
            binding.schoolAddress.text = it.schoolAddress
            binding.schoolName.text = it.schoolName
            binding.schoolWebsite.text = it.schoolWebsite
            binding.schoolDescription.text = it.schoolOverview

            binding.mapView.getMapAsync { map ->
                Log.d(TAG, "populateSchoolDetails: map ready")
                MarkerOptions().apply {
                    position(LatLng(it.schoolLatitude.toDouble(), it.schoolLongitude.toDouble()))
                    title(it.schoolName)
                    visible(true)
                }.also { options -> map.addMarker(options) }
            }
        } ?: Toast.makeText(requireContext(), "Schools details error", Toast.LENGTH_LONG).show()
    }

    private fun populateSchoolSatScores(score: Score? = null) {
        score?.let {
            binding.schoolMathScore.text = it.mathScore
            binding.schoolReadScore.text = it.readScore
            binding.schoolWriteScore.text = it.writeScore
        } ?: Toast.makeText(requireContext(), "Scores unavailable", Toast.LENGTH_LONG).show()
    }

    override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {
        when (p0) {
            MapsInitializer.Renderer.LATEST -> Log.d("MapsDemo", "The latest version of the renderer is used.")
            MapsInitializer.Renderer.LEGACY -> Log.d("MapsDemo", "The legacy version of the renderer is used.")
        }
    }
}