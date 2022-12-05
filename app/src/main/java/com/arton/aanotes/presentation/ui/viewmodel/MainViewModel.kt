package com.arton.aanotes.presentation.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.data.DataStoreManager
import com.arton.aanotes.domain.entity.Action
import com.arton.aanotes.domain.entity.ActionResult
import com.arton.aanotes.domain.repo.AuthRepository
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var mainStateFlow = MutableStateFlow(MainState())
    val mainState = mainStateFlow.asStateFlow()

    private val authFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val auth = authFlow.asStateFlow()

    private val actionFlow: MutableStateFlow<Pair<Action?, AuthEvent>?> = MutableStateFlow(null)
    val action = actionFlow.asStateFlow()

    val actionResult = combine(actionFlow, authFlow) { action, authResult ->
        onAuthEventConsumed()
        ActionResult(action?.first, authResult)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ActionResult(null, false)
    )

    fun fireAuthForAction(action: Action, authEvent: AuthEvent = AuthEvent.AuthenticateAction) {
        actionFlow.update { action to authEvent }
    }

    fun onAuthSucceed() {
        authFlow.update { true }
    }

    fun onAuthFailed() {
        authFlow.update { false }
    }

    private fun onAuthEventConsumed() {
        authFlow.update { null }
        actionFlow.update { null }
    }

    fun doesPinExist(context: Context) = authRepository.doesPinExist(context)

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