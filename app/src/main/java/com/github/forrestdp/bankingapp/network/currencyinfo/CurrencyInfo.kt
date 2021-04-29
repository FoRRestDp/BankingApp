package com.github.forrestdp.bankingapp.network.currencyinfo

import com.squareup.moshi.Json

data class CurrencyInfo(
    @Json(name = "Valute")
    val currencies: Map<String, Currency>
)
