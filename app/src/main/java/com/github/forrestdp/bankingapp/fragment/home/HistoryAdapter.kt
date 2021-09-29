package com.github.forrestdp.bankingapp.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.databinding.TransactionsHistoryItemBinding
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.Transaction
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyCode
import com.github.forrestdp.bankingapp.utils.loadWithCrossfade
import java.math.BigDecimal
import java.math.RoundingMode

class HistoryAdapter : ListAdapter<Transaction, HistoryAdapter.ViewHolder>(HistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: TransactionsHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) = with(binding) {
            historyCompanyLogoImage.loadWithCrossfade(transaction.iconUrl)
            historyCompanyNameText.text = transaction.title
            historyDateText.text = transaction.date
            historyAmountInCurrencyText.setTransactionAmountFormatted(
                transaction.amount,
                transaction.currencyCode,
                transaction.currencyMultiplier,
            )
            historyAmountText.setTransactionAmountInCurrencyFormatted(transaction.amount)
        }

        private fun TextView.setTransactionAmountFormatted(
            amount: String?,
            currencyCode: CurrencyCode?,
            multiplier: BigDecimal?,
        ) {
            if (amount != null && currencyCode != null && multiplier != null) {
                val char = when (currencyCode) {
                    CurrencyCode.USD -> "$"
                    CurrencyCode.GBP -> "£"
                    CurrencyCode.EUR -> "€"
                    CurrencyCode.RUB -> "₽"
                }
                val roundedAmount = (amount.toBigDecimal() * multiplier)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString()
                text = context.getString(
                        R.string.history_transaction_amount_in_currency_string,
                        char,
                        roundedAmount,
                    )
            }
        }

        private fun TextView.setTransactionAmountInCurrencyFormatted(amount: String?) {
            if (amount != null) {
                text = context.getString(
                    R.string.history_transaction_amount_string,
                    "$",
                    amount,
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionsHistoryItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
        oldItem == newItem
}
