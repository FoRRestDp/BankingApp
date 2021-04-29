package com.github.forrestdp.bankingapp.home

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.utils.CurrencyCode

@BindingAdapter("transactionAmountInCurrencyFormatted", "transactionCurrencyBadgeFormatted", "transactionCurrencyMultiplier")
fun TextView.setTransactionAmountFormatted(amount: String?, currencyCode: CurrencyCode?, multiplier: Double?) {
    if (amount != null && currencyCode != null && multiplier != null) {
        val char = when (currencyCode) {
            CurrencyCode.USD -> "$"
            CurrencyCode.GBP -> "£"
            CurrencyCode.EUR -> "€"
            CurrencyCode.RUB -> "₽"
        }
        text =
            this.context.getString(R.string.history_transaction_amount_in_currency_string, char, (amount.toDouble() * multiplier).toString())
    }
}

@BindingAdapter("transactionAmountFormatted")
fun TextView.setTransactionAmountInCurrencyFormatted(amount: String?) {
    if (amount != null) {
        text = this.context.getString(R.string.history_transaction_amount_string, "$", amount)
    }
}

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imageUrl: String?) {
    if (imageUrl != null) {
        val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imageUri)
            .into(this)
    }
}