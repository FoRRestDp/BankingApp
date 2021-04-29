package com.github.forrestdp.bankingapp.network.currencyinfo

import com.squareup.moshi.Json
import java.math.BigDecimal

data class Currency(
    @Json(name = "Value")
    val value: BigDecimal
) {
    companion object {
        val defaultCurrency = Currency(1.0.toBigDecimal())
    }
}
