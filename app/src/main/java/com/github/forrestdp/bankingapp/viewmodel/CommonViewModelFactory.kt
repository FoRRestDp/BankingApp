package com.github.forrestdp.bankingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommonViewModelFactory(
    private val lastPosition: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonViewModel::class.java)) {
            return CommonViewModel(lastPosition) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}