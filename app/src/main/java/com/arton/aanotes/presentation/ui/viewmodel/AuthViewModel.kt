package com.arton.aanotes.presentation.ui.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.arton.aanotes.R
import com.arton.aanotes.domain.repo.AuthRepository
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var authStateFlow = MutableStateFlow(AuthState(AuthEvent.CreatePin()))
    val authState = authStateFlow.asStateFlow()

    fun setAuthEvent(authEvent: AuthEvent) {
        authStateFlow.update { authState ->
            authState.copy(authEvent = authEvent)
        }
    }

    fun onPinEntered(context: Context, pinCode: String): Boolean {
        return when (val authEvent = authStateFlow.value.authEvent) {
            is AuthEvent.AuthenticateAction, is AuthEvent.Login -> {
                val isPinCorrect = authRepository.isPinCorrect(context, pinCode)
                authStateFlow.update { authState ->
                    authState.copy(
                        error = if (!isPinCorrect) R.string.auth_error_wrong_pin else null,
                        isComplete = isPinCorrect
                    )
                }
                isPinCorrect
            }
            is AuthEvent.UpdatePin -> {
                authEvent.oldPin.ifBlank {
                    val isPinCorrect = authRepository.isPinCorrect(context, pinCode)
                    authStateFlow.update { authState ->
                        authState.copy(
                            authEvent = authEvent.copy(oldPin = if (isPinCorrect) pinCode else ""),
                            error = if (!isPinCorrect) R.string.auth_error_wrong_pin else null
                        )
                    }
                    return isPinCorrect
                }
                authEvent.newPin.ifBlank {
                    authRepository.createOrUpdatePin(context, pinCode)
                    authStateFlow.update { authState ->
                        authState.copy(
                            authEvent = authEvent.copy(newPin = pinCode),
                            error = null,
                            isComplete = true
                        )
                    }
                    return true
                }
                return true
            }
            is AuthEvent.CreatePin -> {
                authEvent.pin1.ifBlank {
                    authRepository.createOrUpdatePin(context, pinCode)
                    authStateFlow.update { authState ->
                        authState.copy(
                            authEvent = authEvent.copy(pin1 = pinCode),
                            error = null
                        )
                    }
                    return true
                }
                authEvent.pin2.ifBlank {
                    val isPinCorrect = authRepository.isPinCorrect(context, pinCode)
                    authStateFlow.update { authState ->
                        authState.copy(
                            authEvent = authEvent.copy(pin2 = if (!isPinCorrect) "" else pinCode, pin1 = if (!isPinCorrect) "" else authEvent.pin1),
                            error = if (!isPinCorrect) R.string.auth_error_pins_not_match else null,
                            isComplete = isPinCorrect
                        )
                    }
                    if (!isPinCorrect) {
                        authRepository.clearPin(context)
                        return false
                    } else {
                        return true
                    }
                }
                return true
            }
        }
    }
}

data class AuthState(
    val authEvent: AuthEvent,
    @StringRes val error: Int? = null,
    val isComplete: Boolean = false
)