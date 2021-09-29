package com.github.forrestdp.bankingapp.cards


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.forrestdp.bankingapp.databinding.CardsListItemBinding
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.ShrunkCardInfo
import com.github.forrestdp.bankingapp.utils.setCardTypeImage

class CardsAdapter(
    private val selectedCardPosition: Int,
    private val onClick: (position: Int) -> Unit,
) : ListAdapter<ShrunkCardInfo, CardsAdapter.ViewHolder>(CardsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, position == selectedCardPosition, onClick)
    }

    class ViewHolder private constructor(private val binding: CardsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ShrunkCardInfo,
            position: Int,
            isSelected: Boolean,
            onItemClick: (position: Int) -> Unit,
        ) = with(binding) {
            binding.root.setOnClickListener {
                onItemClick(position)
            }
            cardsCardTypeImage.setCardTypeImage(item.cardType)
            cardsCardNumberText.text = item.cardNumber
            cardsSelectedIndicatorImage.isVisible = isSelected
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

private class CardsDiffCallback : DiffUtil.ItemCallback<ShrunkCardInfo>() {
    override fun areItemsTheSame(oldItem: ShrunkCardInfo, newItem: ShrunkCardInfo): Boolean =
        oldItem.cardNumber == newItem.cardNumber

    override fun areContentsTheSame(oldItem: ShrunkCardInfo, newItem: ShrunkCardInfo): Boolean =
        oldItem == newItem

}