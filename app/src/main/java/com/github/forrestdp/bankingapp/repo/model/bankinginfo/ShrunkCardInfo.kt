package com.github.forrestdp.bankingapp.repo.model.bankinginfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShrunkCardInfo(
    val cardNumber: String,
    val cardType: String,
)