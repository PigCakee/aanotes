package com.arton.aanotes.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arton.aanotes.presentation.ui.components.AANotesAppBar
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.AANotesSnackbar
import com.arton.aanotes.presentation.ui.components.BottomNavigationBar
import com.arton.aanotes.presentation.ui.composable.navigation.AANotesNavGraph
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel

@Composable
fun AANotesApp(mainViewModel: MainViewModel) {
    AANotesTheme {
        val appState = rememberAANotesAppState()

        AANotesScaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    BottomNavigationBar(appState)
                }
            },
            scaffoldState = appState.scaffoldState,
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> AANotesSnackbar(snackbarData) }
                )
            }
        ) { innerPaddingModifier ->
            Box {
                AANotesNavGraph(
                    appState = appState,
                    modifier = Modifier.padding(innerPaddingModifier),
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}