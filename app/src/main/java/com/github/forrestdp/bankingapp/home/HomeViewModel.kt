package com.github.forrestdp.bankingapp.home

import androidx.lifecycle.*
import com.github.forrestdp.bankingapp.network.bankinginfo.CardHoldersApi
import com.github.forrestdp.bankingapp.network.bankinginfo.CardUser
import com.github.forrestdp.bankingapp.network.bankinginfo.Transaction
import com.github.forrestdp.bankingapp.network.currencyinfo.Currency
import com.github.forrestdp.bankingapp.network.currencyinfo.CurrencyInfoApi
import com.github.forrestdp.bankingapp.utils.CurrencyCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val _currentUser = MutableLiveData(CardUser.defaultUser)
    private val _currentCurrency = MutableLiveData(CurrencyCode.USD)

    private lateinit var users: List<CardUser>
    private lateinit var currencies: Map<String, Currency>

    val cardNumber: LiveData<String> = _currentUser.map { it.cardNumber }
    val cardholderName: LiveData<String> = _currentUser.map { it.cardholderName }
    val validThruDate: LiveData<String> = _currentUser.map { it.validThruDate }
    val balance: LiveData<String> = _currentUser.map { "$${it.balance}" }
    val currencyBalance = MediatorLiveData<String>()
    val transactionHistory: LiveData<List<Transaction>> = _currentUser.map {
        it.transactionHistory
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            users = CardHoldersApi.retrofitService.getBankingInfo().users
            _currentUser.postValue(users[1])
            currencies = CurrencyInfoApi.retrofitService.getCurrencyInfo().currencies

            withContext(Dispatchers.Main) {
                currencyBalance.addSource(balance) {
                    renewCurrencyBalance(it!!, _currentCurrency.value!!)
                }
                currencyBalance.addSource(_currentCurrency) {
                    renewCurrencyBalance(balance.value!!, it!!)
                }
            }
        }
    }

    private fun renewCurrencyBalance(balance: String, code: CurrencyCode) {
        val balanceDouble = balance.drop(1).toDouble()
        val currencyCoeff = when (code) {
            CurrencyCode.USD -> currencies.getValue("USD").value
            CurrencyCode.GBP -> currencies.getValue("GBP").value
            CurrencyCode.EUR -> currencies.getValue("EUR").value
            CurrencyCode.RUB -> 1.0
        }
        currencyBalance.value =
            ((currencies.getValue("USD").value * balanceDouble) / currencyCoeff).toString()
    }

    val isGbpChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.GBP }
    val isEurChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.EUR }
    val isRubChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.RUB }

    fun toggleGbp() {
        toggleCurrency(CurrencyCode.GBP)
    }

    fun toggleEur() {
        toggleCurrency(CurrencyCode.EUR)
    }

    fun toggleRub() {
        toggleCurrency(CurrencyCode.RUB)
    }

    private fun toggleCurrency(currencyCode: CurrencyCode) {
        if (_currentCurrency.value == currencyCode) {
            _currentCurrency.value = CurrencyCode.USD
        } else {
            _currentCurrency.value = currencyCode
        }
    }
}