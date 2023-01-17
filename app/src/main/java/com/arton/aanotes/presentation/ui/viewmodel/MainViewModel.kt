package com.arton.aanotes.presentation.ui.viewmodel

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.data.DataStoreManager
import com.arton.aanotes.domain.entity.Action
import com.arton.aanotes.domain.entity.ActionResult
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.repo.AuthRepository
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val authFlow: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val auth = authFlow.asStateFlow()

    private val actionFlow: MutableStateFlow<Pair<Action?, AuthEvent>?> = MutableStateFlow(null)
    val action = actionFlow.asStateFlow()

    private val shareFlow: MutableStateFlow<Note?> = MutableStateFlow(null)
    val share = shareFlow.asSharedFlow()

    val screenCaptureEnabled = dataStoreManager.isScreenCaptureEnabled

    val actionResult = combine(actionFlow, authFlow) { action, authResult ->
        ActionResult(action?.first, authResult)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ActionResult(null, false)
    )

    fun onShareEventConsumed() {
        shareFlow.update { null }
    }

    fun shareNote(note: Note) {
        shareFlow.update { note }
    }

    fun fireAuthForAction(action: Action, authEvent: AuthEvent = AuthEvent.AuthenticateAction) {
        actionFlow.update { action to authEvent }
    }

    fun enableSharing(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setSharingEnabled(enabled)
        }
    }

    fun enableScreenCapture(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setScreenCaptureEnabled(enabled)
        }
    }

    fun onAuthSucceed() {
        authFlow.update { true }
        viewModelScope.launch {
            val seconds = dataStoreManager.authCooldownSeconds.first()
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(seconds.toLong()), 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    authFlow.update { false }
                }
            }.start()
        }
    }

    fun onAuthFailed() {
        authFlow.update { false }
    }

    fun doesPinExist(context: Context) = authRepository.doesPinExist(context)
}

data class MainState(
    val isOnBoardingPassed: Boolean = false
)