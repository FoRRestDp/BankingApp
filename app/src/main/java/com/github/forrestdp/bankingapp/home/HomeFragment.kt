package com.github.forrestdp.bankingapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.forrestdp.bankingapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = homeViewModel

        val adapter = HistoryAdapter()
        binding.homeTransactionHistoryList.adapter = adapter

        homeViewModel.transactionHistory.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        return binding.root
    }
}