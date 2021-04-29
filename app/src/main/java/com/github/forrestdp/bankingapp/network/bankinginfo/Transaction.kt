package com.github.forrestdp.bankingapp.network.bankinginfo

import com.squareup.moshi.Json
import java.util.*

data class Transaction(
    @Transient
    val id: UUID = UUID.randomUUID(),
    val title: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    val date: String,
    val amount: String,
)
