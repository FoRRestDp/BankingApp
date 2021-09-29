package com.github.forrestdp.bankingapp.repo.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

object BigDecimalAdapter {
    @FromJson
    fun fromJson(json: String) = BigDecimal(json)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}