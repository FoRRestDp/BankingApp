package com.github.forrestdp.bankingapp.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.forrestdp.bankingapp.utils.ShrunkCardInfo

class CardsViewModel(
    shrunkCardInfos: List<ShrunkCardInfo>,
    selectedPos: Int,
) : ViewModel() {
    val cardsList: LiveData<List<ShrunkCardInfo>> = MutableLiveData(shrunkCardInfos)
    val selectedPosition: LiveData<Int> = MutableLiveData(selectedPos)
}