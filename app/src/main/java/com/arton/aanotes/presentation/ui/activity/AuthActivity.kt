package com.arton.aanotes.presentation.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.AuthScreen
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.activity.contract.AuthResultContract
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.BlueMain
import com.arton.aanotes.presentation.ui.theme.White
import com.arton.aanotes.presentation.ui.viewmodel.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : FragmentActivity() {

    private val successIntent = Intent().putExtra(
        AuthResultContract.AUTH_RESULT_KEY,
        AuthResultContract.AUTH_SUCCEEDED
    )

    private val failureIntent = Intent().putExtra(
        AuthResultContract.AUTH_RESULT_KEY,
        AuthResultContract.AUTH_FAILED
    )

    private val canUseBiometrics by lazy {
        BiometricManager.from(this)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private val biometricsIgnoredErrors = listOf(
        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
        BiometricPrompt.ERROR_CANCELED,
        BiometricPrompt.ERROR_USER_CANCELED,
        BiometricPrompt.ERROR_NO_BIOMETRICS
    )

    @Suppress("DEPRECATION")
    private val authEvent: AuthEvent by lazy {
        val event: AuthEvent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AuthResultContract.EXTRA_AUTH_EVENT, AuthEvent::class.java)
        } else {
            intent.getParcelableExtra(AuthResultContract.EXTRA_AUTH_EVENT)
        }
        try {
            requireNotNull(event)
        } catch (e: IllegalArgumentException) {
            finishAffinity()
            AuthEvent.AuthenticateAction // unreachable
        }
    }

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setAuthEvent(authEvent)
        setContent {
            AANotesTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setNavigationBarColor(White)
                systemUiController.setStatusBarColor(BlueMain)
                AuthScreen(
                    viewModel = viewModel,
                    canUseBiometrics = canUseBiometrics && (authEvent is AuthEvent.AuthenticateAction || authEvent is AuthEvent.Login),
                    onAuthSuccess = {
                        setResult(RESULT_OK, successIntent)
                        finish()
                    },
                    onBiometricActionRequest = {
                        showBiometricPrompt(authEvent)
                    }
                )
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            when (authEvent) {
                is AuthEvent.AuthenticateAction, is AuthEvent.UpdatePin -> {
                    setResult(RESULT_OK, failureIntent)
                    finish()
                }
                else -> {
                    finishAffinity()
                }
            }
        }
    }

    private fun setBiometricPromptInfo(
        title: String,
        description: String
    ) = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setDescription(description)
        .setNegativeButtonText(getString(R.string.cancel)).build()

    private fun showBiometricPrompt(authEvent: AuthEvent) {
        val biometricPrompt = BiometricPrompt(
            this,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    if (errorCode !in biometricsIgnoredErrors) {

                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    setResult(RESULT_OK, successIntent)
                    finish()
                }

                override fun onAuthenticationFailed() {
                }
            }
        )
        biometricPrompt.authenticate(
            setBiometricPromptInfo(
                title = getString(authEvent.getTitleRes()),
                description = getString(authEvent.getDescriptionRes(true))
            )
        )
    }

    override fun onResume() {
        if (canUseBiometrics && (authEvent is AuthEvent.AuthenticateAction || authEvent is AuthEvent.Login)) {
            showBiometricPrompt(authEvent)
        }
        super.onResume()
    }
}