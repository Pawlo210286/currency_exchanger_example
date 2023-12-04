package com.example.exchanger.datasource.local

import com.example.core.extensions.post
import com.example.exchanger.data.domain.ProfileState
import com.example.exchanger.data.domain.Wallet
import com.example.transfer.extension.fromAsyncAction
import com.example.transfer.model.Transfer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.CoroutineContext

internal class ProfileStateProviderImpl(
    override val coroutineContext: CoroutineContext,
) : ProfileStateProvider, CoroutineScope {

    private val _profileState: MutableStateFlow<ProfileState> =
        MutableStateFlow(ProfileState.getInitial())
    override val profileState: StateFlow<ProfileState>
        get() = _profileState.asStateFlow()

    override val requiredProfileState: ProfileState
        get() = profileState.value

    override suspend fun resetProfile(): Transfer<Unit> = Transfer.fromAsyncAction {
        val initialState = ProfileState.getInitial()
        update(
            transactionCount = initialState.transactionCount,
            wallets = initialState.wallets,
        )
    }

    override suspend fun updateProfile(newProfileState: ProfileState): Transfer<Unit> =
        Transfer.fromAsyncAction {
            update(
                id = newProfileState.id,
                transactionCount = newProfileState.transactionCount,
                wallets = newProfileState.wallets,
            )
        }

    private suspend fun update(
        id: Long = profileState.value.id,
        transactionCount: Int = profileState.value.transactionCount,
        wallets: List<Wallet> = profileState.value.wallets
    ) {
        val newState = ProfileState(
            id = id,
            transactionCount = transactionCount,
            wallets = wallets,
        )
        _profileState.post(newState)
    }
}