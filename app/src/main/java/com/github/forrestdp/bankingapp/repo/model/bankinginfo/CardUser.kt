package com.github.forrestdp.bankingapp.repo.model.bankinginfo

import com.squareup.moshi.Json
import java.math.BigDecimal
import java.util.*

data class CardUser(
    @Transient
    val id: UUID = UUID.randomUUID(),
    @Json(name = "card_number")
    val cardNumber: String,
    @Json(name = "type")
    val cardType: String,
    @Json(name = "cardholder_name")
    val cardholderName: String,
    @Json(name = "valid")
    val validThruDate: String,
    val balance: BigDecimal,
    @Json(name = "transaction_history")
    val transactionHistory: List<Transaction>,
) {
    companion object {
        val defaultUser = CardUser(
            UUID.randomUUID(),
            "0000 0000 0000 0000",
            "mastercard",
            "Cardholder Name",
            "00/00",
            (-1.0).toBigDecimal(),
            emptyList(),
        )
    }
}