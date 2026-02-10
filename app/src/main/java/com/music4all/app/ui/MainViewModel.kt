package com.music4all.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.music4all.app.data.preferences.UserPreferencesRepository
import com.music4all.app.domain.MusicService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the settings/onboarding screen.
 * Exposes the selected service as a StateFlow and handles user selections.
 */
class MainViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val selectedService: StateFlow<MusicService> =
        preferencesRepository.preferredServiceFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MusicService.SPOTIFY
            )

    val isOnboardingComplete: StateFlow<Boolean> =
        preferencesRepository.isOnboardingCompleteFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun onServiceSelected(service: MusicService) {
        viewModelScope.launch {
            preferencesRepository.setPreferredService(service)
        }
    }

    /**
     * Factory to inject the UserPreferencesRepository into the ViewModel.
     */
    class Factory(
        private val preferencesRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(preferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
