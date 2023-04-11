package com.example.impressmap.ui.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.impressmap.ui.activity.MainViewModel

fun Fragment.getMainViewModel(): MainViewModel {
    return ViewModelProvider(requireActivity())[MainViewModel::class.java]
}