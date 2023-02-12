package com.arton.aanotes.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arton.aanotes.data.entity.Action
import com.arton.aanotes.data.entity.Note
import com.arton.aanotes.presentation.ui.AANotesApp
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.activity.contract.AuthResultContract
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authLauncher = registerForActivityResult(AuthResultContract()) { authResult ->
        authResult?.let {
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.actionResult.collect { actionResult ->
                    if (actionResult.authenticated == true) {
                        when (actionResult.action) {
                            is Action.TurnOnScreenCapture -> {
                                viewModel.enableScreenCapture(true)
                                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                            }
                            is Action.TurnOffScreenCapture -> {
                                viewModel.enableScreenCapture(false)
                                window.setFlags(
                                    WindowManager.LayoutParams.FLAG_SECURE,
                                    WindowManager.LayoutParams.FLAG_SECURE
                                )
                            }
                            is Action.TurnOffSharing -> {
                                viewModel.enableSharing(false)
                            }
                            is Action.TurnOnSharing -> {
                                viewModel.enableSharing(true)
                            }
                            else -> {}
                        }
                    }
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.share.collect {
                    it?.let {
                        shareNote(it)
                        viewModel.onShareEventConsumed()
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.screenCaptureEnabled.collect {
                    if (it) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                    } else {
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE
                        )
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

    private fun shareNote(note: Note) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.body}")
        shareIntent.type = "text/plain"

        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun onResume() {
        if (!viewModel.doesPinExist(this)) {
            authLauncher.launch(AuthEvent.CreatePin())
        } else if (viewModel.auth.value == false) {
            authLauncher.launch(AuthEvent.Login)
        }
        super.onResume()
    }
}