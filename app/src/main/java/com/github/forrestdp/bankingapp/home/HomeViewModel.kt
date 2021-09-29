package com.github.forrestdp.bankingapp.home

import androidx.lifecycle.*
import com.github.forrestdp.bankingapp.repo.local.CardPreferencesRepository
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.CardUser
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.Transaction
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.Currency
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyCode
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BankingInfoApiService
import com.github.forrestdp.bankingapp.repo.network.currencyinfo.CurrencyInfoApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: CardPreferencesRepository,
    private val bankingInfoApi: BankingInfoApiService,
    private val currencyInfoApi: CurrencyInfoApiService,
) : ViewModel() {
    private val _currentCurrency = MutableStateFlow(CurrencyCode.USD)
    val currentCurrency = _currentCurrency.asStateFlow()

    private val _loadingStatus = MutableStateFlow(LoadingStatus.LOADING)
    val loadingStatus = _loadingStatus.asStateFlow()

    private val users = MutableStateFlow(emptyList<CardUser>())
    private val _currencies = MutableStateFlow(emptyMap<String,Currency>())

    val currentPosition = repository.lastPosition.filterNotNull().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        0,
    )

    private val _currentUser: Flow<CardUser> =
        users.combine(currentPosition) { list, position ->
            getNewUser(list, position)
        }

    val cardNumber: Flow<String> = _currentUser.map { it.cardNumber }
    val cardholderName: Flow<String> = _currentUser.map { it.cardholderName }
    val validThruDate: Flow<String> = _currentUser.map { it.validThruDate }
    val balance: Flow<String> =
        _currentUser.map { "$ ${it.balance.setScale(2, RoundingMode.HALF_UP)}" }
    val cardType: Flow<String> = _currentUser.map { it.cardType }

    val balanceInCurrency: Flow<String> =
        balance.combine(_currentCurrency) { balance, code ->
            getNewCurrencyBalance(balance, code)
        }


    private val _navigateToCards = MutableSharedFlow<Unit>()
    val navigateToCards = _navigateToCards.asSharedFlow()

    val transactionHistory: Flow<List<Transaction>> =
        combine(_currentUser, _currentCurrency, _currencies) { user, code, currencies ->
            user.transactionHistory.map { transaction ->
                val currencyCoeff = when (code) {
                    CurrencyCode.USD -> currencies["USD"]?.value ?: 1.0.toBigDecimal()
                    CurrencyCode.GBP -> currencies["GBP"]?.value ?: 1.0.toBigDecimal()
                    CurrencyCode.EUR -> currencies["EUR"]?.value ?: 1.0.toBigDecimal()
                    CurrencyCode.RUB -> 1.0.toBigDecimal()
                }
                transaction.copy(
                    currencyCode = code,
                    currencyMultiplier = currencies["USD"]?.value?.div(currencyCoeff) ?: 1.0.toBigDecimal()
                )
            }
        }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadingStatus.value = LoadingStatus.LOADING
                users.value = bankingInfoApi.getBankingInfo().users
                _currencies.value = currencyInfoApi.getCurrencyInfo().currencies
                _loadingStatus.value = LoadingStatus.DONE
            } catch (e: Exception) {
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    private fun getNewUser(list: List<CardUser>, pos: Int): CardUser =
        when {
            list.isEmpty() -> CardUser.defaultUser
            pos !in list.indices -> list[0]
            else -> list[pos]
        }

    private fun getNewCurrencyBalance(balance: String, code: CurrencyCode): String {
        val balanceDouble = if (balance.isNotEmpty()) {
            balance.drop(2).toBigDecimal()
        } else {
            (-1.0).toBigDecimal()
        }
        val (currencyCoeff, badge) = when (code) {
            CurrencyCode.USD -> (_currencies.value["USD"]?.value ?: 1.0.toBigDecimal()) to "$"
            CurrencyCode.GBP -> (_currencies.value["GBP"]?.value ?: 1.0.toBigDecimal()) to "£"
            CurrencyCode.EUR -> (_currencies.value["EUR"]?.value ?: 1.0.toBigDecimal()) to "€"
            CurrencyCode.RUB -> 1.0.toBigDecimal() to "₽"
        }
        return "$badge ${
            ((_currencies.value["USD"]?.value ?: 1.0.toBigDecimal() * balanceDouble) / currencyCoeff).setScale(
                2,
                RoundingMode.HALF_UP
            )
        }"
    }

    fun startNavigatingToCards() {
        viewModelScope.launch {
            _navigateToCards.emit(Unit)
        }
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