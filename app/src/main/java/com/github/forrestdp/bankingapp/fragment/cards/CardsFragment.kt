package com.github.forrestdp.bankingapp.fragment.cards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.databinding.FragmentCardsBinding
import com.github.forrestdp.bankingapp.utils.launchAndCollectIn
import com.github.forrestdp.bankingapp.viewmodel.CardsViewModel
import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val viewModel: CardsViewModel by viewModels()

    private val binding: FragmentCardsBinding by viewBinding(FragmentCardsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CardsAdapter(
            viewModel.currentPosition.value,
            onClick = {
                viewModel.setNewUser(it)
                findNavController().navigateUp()
            }
        )
        binding.cardsList.adapter = adapter

        viewModel.shrunkCardInfos.launchAndCollectIn(viewLifecycleOwner) {
            it.let { adapter.submitList(it) }
        }
        binding.backButtonImg.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}