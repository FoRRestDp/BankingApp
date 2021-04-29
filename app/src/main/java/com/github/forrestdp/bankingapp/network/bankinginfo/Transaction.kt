package com.github.forrestdp.bankingapp.network.bankinginfo

import com.github.forrestdp.bankingapp.utils.CurrencyCode
import com.squareup.moshi.Json
import java.math.BigDecimal
import java.util.*

data class Transaction(
    @Transient
    val id: UUID = UUID.randomUUID(),
    @Transient
    val currencyCode: CurrencyCode = CurrencyCode.USD,
    @Transient
    val currencyMultiplier: BigDecimal = 1.0.toBigDecimal(),
    val title: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    val date: String,
    val amount: String,
)
