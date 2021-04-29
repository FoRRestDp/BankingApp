package com.github.forrestdp.bankingapp.network.bankinginfo

import com.github.forrestdp.bankingapp.utils.CurrencyCode
import com.squareup.moshi.Json
import java.util.*

data class Transaction(
    @Transient
    val id: UUID = UUID.randomUUID(),
    @Transient
    val currencyCode: CurrencyCode = CurrencyCode.USD,
    @Transient
    val currencyMultiplier: Double = 1.0,
    val title: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    val date: String,
    val amount: String,
)
