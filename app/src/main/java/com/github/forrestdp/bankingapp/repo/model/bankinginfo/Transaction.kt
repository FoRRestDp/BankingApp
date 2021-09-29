package com.github.forrestdp.bankingapp.repo.model.bankinginfo

import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.util.*

@JsonClass(generateAdapter = true)
data class Transaction(
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
