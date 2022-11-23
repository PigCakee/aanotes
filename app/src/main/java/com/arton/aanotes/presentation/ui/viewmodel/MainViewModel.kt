package com.arton.aanotes.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private var mainStateFlow = MutableStateFlow(MainState())
    val mainState = mainStateFlow.asStateFlow()

    fun authorizeAction() {

    }

    fun checkIfOnBoardingPassed() {
        viewModelScope.launch {
            mainStateFlow.update { mainState ->
                mainState.copy(isOnBoardingPassed = dataStoreManager.isOnBoardingPassed.first())
            }
        }

    }
}

data class MainState(
    val isOnBoardingPassed: Boolean = false
)