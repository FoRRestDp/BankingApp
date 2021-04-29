package com.github.forrestdp.bankingapp.network

import com.squareup.moshi.Json

data class Transaction(
    val title: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    val date: String,
    val amount: String,
)
