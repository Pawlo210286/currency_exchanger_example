package com.example.exchanger.datasource.local

import com.example.exchanger.data.domain.ProfileState
import com.example.transfer.model.Transfer
import kotlinx.coroutines.flow.StateFlow

interface ProfileStateProvider {

    val profileState: StateFlow<ProfileState>

    val requiredProfileState: ProfileState

    suspend fun updateProfile(newProfileState: ProfileState): Transfer<Unit>

    suspend fun resetProfile(): Transfer<Unit>
}