package com.github.forrestdp.bankingapp.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.bankingapp.repo.local.CardPreferencesRepository
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.CardUser
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.ShrunkCardInfo
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BankingInfoApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val repository: CardPreferencesRepository,
    private val bankingInfoApi: BankingInfoApiService,
) : ViewModel() {

    private val users = MutableStateFlow(emptyList<CardUser>())

    init {
        viewModelScope.launch {
            users.value = bankingInfoApi.getBankingInfo().users
        }
    }

    val currentPosition = repository.lastPosition.filterNotNull().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        0,
    )

    val shrunkCardInfos: Flow<List<ShrunkCardInfo>> = users.map { list ->
        list.map { cardUser: CardUser ->
            ShrunkCardInfo(cardUser.cardNumber, cardUser.cardType)
        }
    }

    fun setNewUser(position: Int) {
        viewModelScope.launch {
            repository.setLastPosition(position)
        }
    }
}