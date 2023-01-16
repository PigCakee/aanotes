package com.arton.aanotes.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val settingsState = combine(
        dataStoreManager.isSharingEnabled,
        dataStoreManager.isScreenCaptureEnabled,
        dataStoreManager.authCooldownSeconds
    ) { sharingEnabled, screenCaptureEnabled, authCooldownSeconds ->
        currentCooldown = authCooldownSeconds
        SettingsState(
            sharingEnabled,
            screenCaptureEnabled,
            authCooldownSeconds
        )
    }

    private var currentCooldown = 5

    private fun incrementTime(): Int {
        when (currentCooldown) {
            in 5..10 -> currentCooldown += 5
            15 -> currentCooldown = 30
            30 -> currentCooldown = 60
            60 -> currentCooldown = 90
            90 -> currentCooldown = 120
            120 -> currentCooldown = 5
        }
        return currentCooldown
    }

    fun onNewCooldown() {
        viewModelScope.launch {
            dataStoreManager.setAuthCooldownSeconds(incrementTime())
        }
    }

    data class SettingsState(
        val sharingEnabled: Boolean = false,
        val screenCaptureEnabled: Boolean = false,
        val authCooldownSeconds: Int = 5
    )
}