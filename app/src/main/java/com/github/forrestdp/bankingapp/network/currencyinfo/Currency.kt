package com.github.forrestdp.bankingapp.network.currencyinfo

import com.squareup.moshi.Json

data class Currency(
    @Json(name = "Value")
    val value: Double
) {
    companion object {
        val defaultCurrency = Currency(1.0)
    }
}
