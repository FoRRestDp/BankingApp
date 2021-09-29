package com.github.forrestdp.bankingapp.repo.model.currencyinfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyInfo(
    @Json(name = "Valute")
    val currencies: Map<String, Currency>,
)
