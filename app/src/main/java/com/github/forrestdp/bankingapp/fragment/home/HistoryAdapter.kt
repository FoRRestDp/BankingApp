package com.github.forrestdp.bankingapp.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.forrestdp.bankingapp.databinding.OpertionsHistoryItemBinding
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.Transaction

class HistoryAdapter : ListAdapter<Transaction, HistoryAdapter.ViewHolder>(HistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: OpertionsHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction) {
            binding.transaction = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OpertionsHistoryItemBinding.inflate(layoutInflater, parent ,false)
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
