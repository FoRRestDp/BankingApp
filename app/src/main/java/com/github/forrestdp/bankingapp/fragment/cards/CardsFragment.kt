package com.github.forrestdp.bankingapp.fragment.cards

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.bankingapp.databinding.FragmentCardsBinding
import com.github.forrestdp.bankingapp.viewmodel.CommonViewModel

class CardsFragment : Fragment() {

    private val viewModel: CommonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentCardsBinding.inflate(layoutInflater, container, false)

        val adapter = CardsAdapter(viewModel.currentPosition.value!!, CardListener {
            viewModel.setNewUser(it.toInt())
            findNavController().navigateUp()
        })

        binding.cardsList.adapter = adapter
        Log.i("CardsFragment", "${viewModel.shrunkCardInfos.value}")
        viewModel.shrunkCardInfos.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }
        binding.backButtonImg.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}