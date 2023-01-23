package com.yedebkid.jpmccodingchalleng.util

sealed class UIState {
    object LOADING : UIState()
    data class SUCCESS<out T>(val data: T) : UIState()
    data class ERROR(val error: Exception) : UIState()
}
