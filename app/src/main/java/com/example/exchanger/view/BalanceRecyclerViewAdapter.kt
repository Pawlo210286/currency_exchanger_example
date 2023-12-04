package com.example.exchanger.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.exchanger.data.domain.Wallet
import com.example.exchanger.databinding.ItemCurrencyWalletBinding

internal object WalletDiffUtils : DiffUtil.ItemCallback<Wallet>() {

    override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean = oldItem.currencyId == newItem.currencyId

    override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean = oldItem == newItem
}

internal class BalanceRecyclerViewAdapter: ListAdapter<Wallet, WalletViewHolder>(WalletDiffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WalletViewHolder(ItemCurrencyWalletBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}