package com.yedebkid.jpmccodingchalleng.util

import com.yedebkid.jpmccodingchalleng.model.domain.School

sealed class ItemView {
    data class HeaderItem(val character: String): ItemView()
    data class SchoolItem(val school: School): ItemView()
}

