package com.github.forrestdp.bankingapp.cards

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("adaptImage")
fun ImageView.adaptImage(cardType: String?) {
    if (cardType != null) {
        when (cardType) {
            "mastercard" -> TODO()
            "visa" -> TODO()
            "unionpay" -> TODO()
        }
    }
}