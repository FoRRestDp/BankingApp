package com.github.forrestdp.bankingapp.viewmodel

import androidx.lifecycle.*
import com.github.forrestdp.bankingapp.fragment.home.LoadingStatus
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.CardUser
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.ShrunkCardInfo
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.Transaction
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.Currency
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyCode
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.CardHoldersApi
import com.github.forrestdp.bankingapp.repo.network.currencyinfo.CurrencyInfoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode

class CommonViewModel(lastPosition: Int) : ViewModel() {
    private val _currentUser = MediatorLiveData<CardUser>()
    private val _currentCurrency = MutableLiveData(CurrencyCode.USD)

    private val _loadingStatus = MutableLiveData(LoadingStatus.LOADING)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _users = MutableLiveData<List<CardUser>>()
    private val _currencies = MutableLiveData<Map<String, Currency>>()

    private val _currentPosition = MutableLiveData(lastPosition)
    val currentPosition: LiveData<Int> = _currentPosition

    val shrunkCardInfos: LiveData<List<ShrunkCardInfo>> = _users.map { list ->
        list.mapIndexed { i: Int, cardUser: CardUser ->
            ShrunkCardInfo(i.toLong(), cardUser.cardNumber, cardUser.cardType)
        }
    }

    val cardNumber: LiveData<String> = _currentUser.map { it.cardNumber }
    val cardholderName: LiveData<String> = _currentUser.map { it.cardholderName }
    val validThruDate: LiveData<String> = _currentUser.map { it.validThruDate }
    val balance: LiveData<String> =
        _currentUser.map { "$ ${it.balance.setScale(2, RoundingMode.HALF_UP)}" }
    val cardType: LiveData<String> = _currentUser.map { it.cardType }

    private val _balanceInCurrency = MediatorLiveData<String>()
    val balanceInCurrency: LiveData<String> = _balanceInCurrency

    private val _navigateToCards = MutableLiveData<Boolean?>()
    val navigateToCards: LiveData<Boolean?> = _navigateToCards

    private val _transactionHistory = MediatorLiveData<List<Transaction>>()
    val transactionHistory: LiveData<List<Transaction>> = _transactionHistory

    val isGbpChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.GBP }
    val isEurChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.EUR }
    val isRubChecked: LiveData<Boolean> = _currentCurrency.map { it == CurrencyCode.RUB }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingStatus.postValue(LoadingStatus.LOADING)
                _users.postValue(CardHoldersApi.retrofitService.getBankingInfo().users)
                _currencies.postValue(CurrencyInfoApi.retrofitService.getCurrencyInfo().currencies)

                withContext(Dispatchers.Main) {
                    initCurrentUser()
                    initCurrencyBalance()
                    initTransactionHistory()
                    _loadingStatus.value = LoadingStatus.DONE
                }
            } catch (e: Exception) {
                _loadingStatus.postValue(LoadingStatus.ERROR)
            }
        }
    }

    private fun initCurrentUser() {
        _currentUser.addSource(_users) { list ->
            renewCurrentUser(list, _currentPosition.value ?: 0)
        }
        _currentUser.addSource(_currentPosition) { position ->
            renewCurrentUser(_users.value ?: emptyList(), position)
        }
    }

    private fun renewCurrentUser(list: List<CardUser>, pos: Int) {
        when {
            list.isEmpty() -> _currentUser.value = CardUser.defaultUser
            pos >= list.size || pos <= 0 -> _currentUser.value = list[0]
            else -> _currentUser.value = list[pos]
        }
    }

    private fun initTransactionHistory() {
        _transactionHistory.addSource(_currentUser) { user ->
            _transactionHistory.value =
                user.transactionHistory.map { it.copy(amount = it.amount.drop(1)) }
        }
        _transactionHistory.addSource(_currentCurrency) { code ->
            _transactionHistory.value = _transactionHistory.value?.map { transaction ->
                val currencyCoeff = when (code) {
                    CurrencyCode.USD -> _currencies.value?.get("USD")?.value
                    CurrencyCode.GBP -> _currencies.value?.get("GBP")?.value
                    CurrencyCode.EUR -> _currencies.value?.get("EUR")?.value
                    CurrencyCode.RUB -> 1.0.toBigDecimal()
                    null -> return@addSource
                } ?: return@addSource
                transaction.copy(
                    currencyCode = code,
                    currencyMultiplier = (_currencies.value?.getValue("USD")
                        ?.value?.div(currencyCoeff))
                        ?: throw IllegalStateException("Something went wrong")
                )
            }
        }
    }

    private fun initCurrencyBalance() {
        _balanceInCurrency.addSource(balance) {
            renewCurrencyBalance(it!!, _currentCurrency.value!!)
        }
        _balanceInCurrency.addSource(_currentCurrency) {
            renewCurrencyBalance(balance.value!!, it!!)
        }
    }

    private fun renewCurrencyBalance(balance: String, code: CurrencyCode) {
        val balanceDouble = if (balance.isEmpty())
            balance.drop(2).toBigDecimal()
        else
            (-1.0).toBigDecimal()
        val (currencyCoeff, badge) = when (code) {
            CurrencyCode.USD -> _currencies.value?.getValue("USD")?.value to "$"
            CurrencyCode.GBP -> _currencies.value?.getValue("GBP")?.value to "£"
            CurrencyCode.EUR -> _currencies.value?.getValue("EUR")?.value to "€"
            CurrencyCode.RUB -> 1.0.toBigDecimal() to "₽"
        }
        _balanceInCurrency.value =
            "$badge ${
                ((_currencies.value?.getValue("USD")?.value!! * balanceDouble) / currencyCoeff!!).setScale(
                    2,
                    RoundingMode.HALF_UP
                )
            }"
    }

    fun startNavigatingToCards() {
        _navigateToCards.value = true
    }

    fun doneNavigatingToCards() {
        _navigateToCards.value = null
    }

    fun setNewUser(position: Int) {
        _currentPosition.value = position
    }

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

    fun retryDataLoading() {
        loadData()
    }
}