package com.github.forrestdp.bankingapp.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.bankingapp.databinding.FragmentHomeBinding
import com.github.forrestdp.bankingapp.viewmodel.CommonViewModel

class HomeFragment : Fragment() {
    private val homeViewModel: CommonViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = homeViewModel

        val adapter = HistoryAdapter()
        binding.homeTransactionHistoryList.adapter = adapter

        homeViewModel.transactionHistory.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        homeViewModel.navigateToCards.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToCardsFragment())
                homeViewModel.doneNavigatingToCards()
            }
        }

        return binding.root
    }
}