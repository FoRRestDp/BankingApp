package com.github.forrestdp.bankingapp.repo.model.currencyinfo

import com.github.forrestdp.bankingapp.repo.model.currencyinfo.Currency
import com.squareup.moshi.Json

data class CurrencyInfo(
    @Json(name = "Valute")
    val currencies: Map<String, Currency>
)
