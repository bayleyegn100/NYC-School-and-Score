package com.yedebkid.jpmccodingchalleng.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yedebkid.jpmccodingchalleng.viewmodel.SchoolsViewModel

open class BaseFragment : Fragment() {

    protected val schoolViewModel by lazy {
        ViewModelProvider(requireActivity())[SchoolsViewModel::class.java]
    }
}