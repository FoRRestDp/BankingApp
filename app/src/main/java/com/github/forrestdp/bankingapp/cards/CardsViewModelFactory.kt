package com.github.forrestdp.bankingapp.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.bankingapp.utils.ShrunkCardInfo
import java.lang.IllegalArgumentException

class CardsViewModelFactory(
    private val shrunkCardInfos: List<ShrunkCardInfo>,
    private val selectedPosition: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(shrunkCardInfos, selectedPosition) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}