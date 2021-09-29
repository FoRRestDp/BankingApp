package com.github.forrestdp.bankingapp.repo.model.bankinginfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BankingInfo(val users: List<CardUser>)
