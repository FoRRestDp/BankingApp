package com.github.forrestdp.bankingapp.home

import androidx.lifecycle.*
import com.github.forrestdp.bankingapp.network.CardHoldersApi
import com.github.forrestdp.bankingapp.network.CardUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _currentUser = MutableLiveData(CardUser.defaultUser)

    private lateinit var users: List<CardUser>
    init {
        viewModelScope.launch(Dispatchers.IO) {
            users = CardHoldersApi.retrofitService.getCardInfo().users
            _currentUser.postValue(users[1])
        }
    }

    val cardNumber: LiveData<String> = _currentUser.map { it.cardNumber }
    val cardholderName: LiveData<String> = _currentUser.map { it.cardholderName }
    val validThruDate: LiveData<String> = _currentUser.map { it.validThruDate }
    val balance: LiveData<String> = _currentUser.map { "$${it.balance}" }
    val currencyBalance: LiveData<String> = _currentUser.map { "₽ ${it.balance * 79}" }
}