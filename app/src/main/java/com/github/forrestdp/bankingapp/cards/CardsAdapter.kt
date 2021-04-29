package com.github.forrestdp.bankingapp.cards


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.forrestdp.bankingapp.databinding.CardsListItemBinding
import com.github.forrestdp.bankingapp.utils.ShrunkCardInfo

class CardsAdapter(private val selectedCardPosition: Int, private val clickListener: CardListener) : ListAdapter<ShrunkCardInfo, CardsAdapter.ViewHolder>(CardsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == selectedCardPosition, clickListener)
    }

    class ViewHolder private constructor(private val binding: CardsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShrunkCardInfo, isSelected: Boolean, clickListener: CardListener) {
            binding.cardInfo = item
            if (isSelected) {
                binding.cardsSelectedIndicatorImage.visibility = View.VISIBLE
            } else {
                binding.cardsSelectedIndicatorImage.visibility = View.INVISIBLE
            }
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CardsDiffCallback : DiffUtil.ItemCallback<ShrunkCardInfo>() {
    override fun areItemsTheSame(oldItem: ShrunkCardInfo, newItem: ShrunkCardInfo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShrunkCardInfo, newItem: ShrunkCardInfo): Boolean =
        oldItem == newItem

}

class CardListener(val clickListener: (position: Long) -> Unit) {
    fun onClick(cardInfo: ShrunkCardInfo) = clickListener(cardInfo.id)
}