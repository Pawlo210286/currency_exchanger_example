package com.example.exchanger.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.core.extensions.observeFlow
import com.example.core.extensions.safeObserveLiveEvent
import com.example.core.extensions.setDelayedOnClickListener
import com.example.core.extensions.viewbinding.viewBinding
import com.example.exchanger.R
import com.example.exchanger.data.domain.CurrencyExchangeResult
import com.example.exchanger.data.domain.CurrencyRate
import com.example.exchanger.databinding.FragmentExchangerOverviewBinding
import com.example.exchanger.viewmodel.CurrencyExchangeOverviewViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangerOverviewFragment : Fragment(R.layout.fragment_exchanger_overview) {

    private val binding: FragmentExchangerOverviewBinding by viewBinding()
    private val viewModel: CurrencyExchangeOverviewViewModel by viewModel()

    private val walletAdapted = BalanceRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        viewModel.launchRateSync()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopRateSync()
    }

    private fun setupViews() = with(binding) {
        balanceRecyclerView.apply {
            adapter = walletAdapted
            layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW).apply {
                justifyContent = JustifyContent.SPACE_AROUND
            }
        }

        buyButton.setDelayedOnClickListener {
            val amountText = buyCurrencyAmountEdit.text?.toString()
            if (amountText.isNullOrBlank()) {
                return@setDelayedOnClickListener
            }

            viewModel.exchangeCurrency(
                amountText.toDouble(),
                (sellCurrencyType.selectedItem as? String).orEmpty(),
                (buyCurrencyType.selectedItem as? String).orEmpty(),
            )
        }

        sellButton.setDelayedOnClickListener {
            val amountText = sellCurrencyAmountEdit.text?.toString()
            if (amountText.isNullOrBlank()) {
                return@setDelayedOnClickListener
            }

            viewModel.exchangeCurrency(
                amountText.toDouble(),
                (buyCurrencyType.selectedItem as? String).orEmpty(),
                (sellCurrencyType.selectedItem as? String).orEmpty(),
            )
        }
    }

    private fun setupObservers() {
        observeFlow(viewModel.state, ::onStateChanged)
        safeObserveLiveEvent(viewModel.event, ::onEvent)
    }

    private fun onStateChanged(state: CurrencyExchangeOverviewViewModel.State) = when (state) {
        is CurrencyExchangeOverviewViewModel.State.Content -> {
            walletAdapted.submitList(state.profileState.wallets)
            refreshSpinners(state.currencyRates)
        }

        CurrencyExchangeOverviewViewModel.State.Error -> {}
        CurrencyExchangeOverviewViewModel.State.Loading -> {}
    }

    private fun onEvent(event: CurrencyExchangeOverviewViewModel.Event) {
        when (event) {
            CurrencyExchangeOverviewViewModel.Event.CurrencyRateUpdatingError -> {
                Toast.makeText(requireContext(), "Currency rate sync error!", Toast.LENGTH_SHORT)
                    .show()
            }

            is CurrencyExchangeOverviewViewModel.Event.CurrencyExchangeTransactionResult -> {
                onCurrencyExchangeResult(event.result)
            }
        }
    }

    private fun onCurrencyExchangeResult(result: CurrencyExchangeResult) {
        when (result) {
            is CurrencyExchangeResult.Error -> {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

            is CurrencyExchangeResult.Success -> {
                showExchangeSuccessDialog(result)
            }
        }
    }

    private fun showExchangeSuccessDialog(result: CurrencyExchangeResult.Success) = with(result) {
        val message = getString(
            R.string.exchange_success_message,
            baseAmountTaken,
            baseCurrencyId,
            targetAmountReceived,
            targetCurrencyId,
            commissionFee,
            targetCurrencyId
        )

        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun refreshSpinners(currencyRates: List<CurrencyRate>) = with(binding) {
        val currencyRatesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            currencyRates.map {
                it.currencyId
            }
        )
        buyCurrencyType.adapter = currencyRatesAdapter
        sellCurrencyType.adapter = currencyRatesAdapter
    }
}