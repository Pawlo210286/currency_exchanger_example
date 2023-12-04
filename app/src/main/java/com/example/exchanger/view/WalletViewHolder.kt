package com.example.exchanger.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exchanger.data.domain.Wallet
import com.example.exchanger.databinding.ItemCurrencyWalletBinding

internal class WalletViewHolder(val binding: ItemCurrencyWalletBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(wallet: Wallet) = with(binding) {
        currencyId.text = wallet.currencyId
        balance.text = wallet.balance.toString()
    }

}