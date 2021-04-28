package com.github.forrestdp.bankingapp.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.forrestdp.bankingapp.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {

    private val cardsViewModel: CardsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentCardsBinding.inflate(layoutInflater)

        return binding.root
    }
}