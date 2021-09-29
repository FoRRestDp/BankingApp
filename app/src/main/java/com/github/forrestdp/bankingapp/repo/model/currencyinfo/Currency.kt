package com.github.forrestdp.bankingapp.repo.model.currencyinfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class Currency(
    @Json(name = "Value")
    val value: BigDecimal,
)
