package com.github.forrestdp.bankingapp.home

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.forrestdp.bankingapp.R
import com.github.forrestdp.bankingapp.databinding.FragmentHomeBinding
import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyCode
import com.github.forrestdp.bankingapp.utils.getColor
import com.github.forrestdp.bankingapp.utils.getDrawable
import com.github.forrestdp.bankingapp.utils.launchAndCollectIn
import com.github.forrestdp.bankingapp.utils.setCardTypeImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        initNavigation()
        initClickListeners()
        initFlowCollectors()
    }

    private fun initRecycler() {
        val adapter = HistoryAdapter()
        binding.homeTransactionHistoryList.adapter = adapter

        viewModel.transactionHistory.launchAndCollectIn(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initNavigation() {
        viewModel.navigateToCards.launchAndCollectIn(viewLifecycleOwner) {
            findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToCardsFragment())
        }
    }

    private fun initClickListeners() = with(binding) {
        gbpButton.setOnClickListener {
            viewModel.toggleGbp()
        }
        eurButton.setOnClickListener {
            viewModel.toggleEur()
        }
        rubButton.setOnClickListener {
            viewModel.toggleRub()
        }
        homeRetryIcon.setOnClickListener {
            viewModel.retryDataLoading()
        }
        cardView.setOnClickListener {
            viewModel.startNavigatingToCards()
        }
    }

    private fun initFlowCollectors() = with(viewModel) {
        loadingStatus.launchAndCollectIn(viewLifecycleOwner) {
            binding.shimmer.setLoadingStatus(it)
            binding.homeRetryIconContainer.isVisible = it == LoadingStatus.ERROR
            binding.homeMainContainer.isVisible = it == LoadingStatus.DONE
        }
        cardNumber.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeCardNumberText.text = it
        }
        cardType.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeBankTypeImage.setCardTypeImage(it)
        }
        cardholderName.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeCardholderNameText.text = it
        }
        validThruDate.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeValidThruText.text = it
        }
        balance.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeBalanceText.text = it
        }
        balanceInCurrency.launchAndCollectIn(viewLifecycleOwner) {
            binding.homeCurrencyBalanceText.text = it
        }
        currentCurrency.launchAndCollectIn(viewLifecycleOwner) {
            binding.gbpButton.setSelectedCustom(it == CurrencyCode.GBP)
            binding.eurButton.setSelectedCustom(it == CurrencyCode.EUR)
            binding.rubButton.setSelectedCustom(it == CurrencyCode.RUB)
        }
    }

    private fun Button.setSelectedCustom(isSelected: Boolean) {
        val unselectedBackground = getDrawable(R.drawable.currency_button_shape_unselected)
        val selectedBackground = getDrawable(R.drawable.currency_button_shape_selected)
        background = if (isSelected) selectedBackground else unselectedBackground

        val unselectedTextColor = getColor(R.color.gray)
        val selectedTextColor = getColor(R.color.white)
        setTextColor(if (isSelected) selectedTextColor else unselectedTextColor)
        compoundDrawables.forEach {
            it?.colorFilter = PorterDuffColorFilter(if (isSelected) selectedTextColor else unselectedTextColor, PorterDuff.Mode.ADD)
        }
    }

    private fun ShimmerFrameLayout.setLoadingStatus(loadingStatus: LoadingStatus?) {
        when (loadingStatus) {
            LoadingStatus.LOADING -> {
                isVisible = true
                startShimmer()
            }
            LoadingStatus.ERROR, LoadingStatus.DONE -> {
                isGone = true
                stopShimmer()
            }
        }
    }
}