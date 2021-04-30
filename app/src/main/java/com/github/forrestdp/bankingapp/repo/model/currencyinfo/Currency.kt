package com.github.forrestdp.bankingapp.repo.model.currencyinfo

import com.squareup.moshi.Json
import java.math.BigDecimal

data class Currency(
    @Json(name = "Value")
    val value: BigDecimal,
)
