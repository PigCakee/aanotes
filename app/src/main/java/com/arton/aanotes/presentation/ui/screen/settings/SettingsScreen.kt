package com.arton.aanotes.presentation.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arton.aanotes.domain.entity.Action
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import com.arton.aanotes.presentation.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    mainViewModel: MainViewModel,
) {

    SettingsScreenUi(onAuthRequired = { action, authEvent ->

    })
}

@Composable
fun SettingsScreenUi(
    onAuthRequired: (Action, AuthEvent) -> Unit
) {

}

@Composable
@Preview
fun PreviewSettings() {
    AANotesTheme {
        SettingsScreenUi(onAuthRequired = { action, authEvent ->

        })
    }
}