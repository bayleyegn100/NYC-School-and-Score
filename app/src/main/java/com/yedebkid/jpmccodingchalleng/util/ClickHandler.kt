package com.yedebkid.jpmccodingchalleng.util

import com.yedebkid.jpmccodingchalleng.model.domain.School

sealed class ClickHandler {
    data class WebsiteClick(val website: String) : ClickHandler()
    data class AddressClick(val longitude: String, val latitude: String) : ClickHandler()
    data class DetailsClick(val school: School) : ClickHandler()
}

