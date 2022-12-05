package com.arton.aanotes.presentation.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.AANotesApp
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.activity.contract.AuthResultContract
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var needAuth: Boolean = true

    private val authLauncher = registerForActivityResult(AuthResultContract()) { authResult ->
        authResult?.let {
            needAuth = false
            when (it) {
                AuthResultContract.AUTH_SUCCEEDED -> {
                    viewModel.onAuthSucceed()
                }
                AuthResultContract.AUTH_FAILED -> {
                    viewModel.onAuthFailed()
                }
            }
        }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.mainState.collect { mainState ->
                    // Я хуй знает так вообще делать или нет гляну потом
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.action.collect { action ->
                    action?.let {
                        authLauncher.launch(it.second)
                    }
                }
            }
        }

        setContent {
            AANotesTheme {
                AANotesApp(mainViewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = getColor(R.color.white)
        if (!needAuth) {
            needAuth = true
        } else {
            if (!viewModel.doesPinExist(this)) {
                authLauncher.launch(AuthEvent.CreatePin())
            } else {
                authLauncher.launch(AuthEvent.Login)
            }
        }
        super.onResume()
    }
}