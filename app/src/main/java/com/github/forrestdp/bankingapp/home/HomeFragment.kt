package com.github.forrestdp.bankingapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
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