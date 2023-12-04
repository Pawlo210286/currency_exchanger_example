package com.example.exchanger.data.domain

data class ProfileState(
    val id: Long,
    val transactionCount: Int,
    val wallets: List<Wallet>
) {

    companion object {
        private const val INITIAL_PROFILE_ID = 1L
        private const val INITIAL_WALLET_CURRENCY_ID = "EUR"
        private const val INITIAL_WALLET_BALANCE = 1000.0
        private const val INITIAL_TRANSACTION_COUNT = 0

        fun getInitial() = ProfileState(
            id = INITIAL_PROFILE_ID,
            transactionCount = INITIAL_TRANSACTION_COUNT,
            wallets = listOf(
                Wallet(
                    currencyId = INITIAL_WALLET_CURRENCY_ID,
                    balance = INITIAL_WALLET_BALANCE,
                )
            )
        )
    }
}
