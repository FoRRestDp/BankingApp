package com.github.forrestdp.bankingapp.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.bankingapp.databinding.FragmentCardsBinding
import com.github.forrestdp.bankingapp.home.HomeViewModel
import com.github.forrestdp.bankingapp.network.bankinginfo.CardUser
import com.github.forrestdp.bankingapp.utils.ShrunkCardInfo

class CardsFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentCardsBinding.inflate(layoutInflater, container, false)

        val adapter = CardsAdapter(viewModel.currentPosition, CardListener {
            viewModel.setNewUser(it.toInt())
            findNavController().navigateUp()
        })

        binding.cardsList.adapter = adapter
        adapter.submitList(viewModel.users.mapIndexed { i: Int, cardUser: CardUser ->
            ShrunkCardInfo(i.toLong(), cardUser.cardNumber, cardUser.cardType)
        })

        return binding.root
    }
}