package com.yedebkid.jpmccodingchalleng

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yedebkid.jpmccodingchalleng.adapter.SchoolsAdapter
import com.yedebkid.jpmccodingchalleng.databinding.FragmentSchoolsBinding
import com.yedebkid.jpmccodingchalleng.model.domain.School
import com.yedebkid.jpmccodingchalleng.util.BaseFragment
import com.yedebkid.jpmccodingchalleng.util.ClickHandler
import com.yedebkid.jpmccodingchalleng.util.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolsFragment : BaseFragment() {

    private val binding by lazy {
        FragmentSchoolsBinding.inflate(layoutInflater)
    }

    private val schoolAdapter by lazy {
        SchoolsAdapter { handler ->
            when(handler) {
                is ClickHandler.WebsiteClick -> {
                    Uri.parse("https://${handler.website}").also { uri ->
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }
                is ClickHandler.AddressClick -> {
                    Uri.parse("geo:0,0?q=${handler.latitude}, ${handler.longitude}").also { location ->
                        activity?.startActivity(Intent(Intent.ACTION_VIEW, location))
                    }
                }
                is ClickHandler.DetailsClick -> {
                    schoolViewModel.school = handler.school
                    findNavController().navigate(
                        R.id.action_SchoolsFragment_to_DetailsFragment
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.schoolsRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = schoolAdapter
        }

        val accessibilityManager = requireContext().getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        if (accessibilityManager.isEnabled) {
            val event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT)
            event.text.add("HELLO")
            accessibilityManager.sendAccessibilityEvent(event)
        }

        schoolViewModel.schools.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.LOADING -> {
                    binding.schoolsRv.visibility = View.GONE
                    binding.loadingSchools.visibility = View.VISIBLE
                }
                is UIState.SUCCESS<*> -> {
                    binding.schoolsRv.visibility = View.VISIBLE
                    binding.loadingSchools.visibility = View.GONE

                    val newSchools = state.data as List<School>
                    schoolAdapter.updateSchools(newSchools)
                }
                is UIState.ERROR -> {
                    binding.schoolsRv.visibility = View.GONE
                    binding.loadingSchools.visibility = View.GONE
                }
            }
        }

        return binding.root
    }
}