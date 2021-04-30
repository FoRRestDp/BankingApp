package com.github.forrestdp.bankingapp.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.databinding.FragmentHomeBinding
import com.github.forrestdp.bankingapp.viewmodel.CommonViewModel
import com.github.forrestdp.bankingapp.viewmodel.CommonViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel: CommonViewModel by activityViewModels {
        CommonViewModelFactory(
            activity?.getPreferences(Context.MODE_PRIVATE)
                ?.getInt(getString(R.string.preference_last_position_key), 0)
                ?: 0
        )
    }
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeViewModel = viewModel

        val adapter = HistoryAdapter()
        binding.homeTransactionHistoryList.adapter = adapter

        viewModel.transactionHistory.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        viewModel.navigateToCards.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToCardsFragment())
                viewModel.doneNavigatingToCards()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        val preferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(preferences.edit()) {
            putInt(getString(R.string.preference_last_position_key), viewModel.currentPosition.value ?: 0)
            apply()
        }
    }
}