package com.example.exchanger.usecase

import com.example.exchanger.data.domain.ProfileState
import com.example.exchanger.datasource.local.ProfileStateProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetProfileStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val profileStateProvider: ProfileStateProvider,
) {

    suspend operator fun invoke(): Flow<ProfileState> = withContext(dispatcher) {
        profileStateProvider.profileState
    }

}