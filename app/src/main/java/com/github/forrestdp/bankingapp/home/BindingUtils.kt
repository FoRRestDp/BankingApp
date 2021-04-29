package com.github.forrestdp.bankingapp.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.network.bankinginfo.Transaction

@BindingAdapter("transactionAmountFormatted")
fun TextView.setTransactionAmountFormatted(item: Transaction?) {
    if (item != null) {
        text =
            this.context.getString(R.string.money_with_currency, "₽", item.amount.toDouble() * 70)
    }
}

@BindingAdapter("transactionAmountInCurrencyFormatted")
fun TextView.setTransactionAmountInCurrencyFormatted(item: Transaction?) {
    if (item != null) {
        text = this.context.getString(R.string.money_with_currency, "$", item.amount.toDouble())
    }
}