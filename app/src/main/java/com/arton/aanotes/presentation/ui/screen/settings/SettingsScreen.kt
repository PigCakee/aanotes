package com.arton.aanotes.presentation.ui.screen.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.R
import com.arton.aanotes.domain.entity.Action
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.Dialog
import com.arton.aanotes.presentation.ui.components.SettingsAppBar
import com.arton.aanotes.presentation.ui.components.SettingsButton
import com.arton.aanotes.presentation.ui.theme.*
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import com.arton.aanotes.presentation.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    mainViewModel: MainViewModel,
) {
    val settingsState by settingsViewModel.settingsState.collectAsState(SettingsViewModel.SettingsState())

    AANotesScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            SettingsAppBar()
        }
    ) {
        SettingsScreenUi(
            settingsState = settingsState,
            onAuthRequired = { action, authEvent ->
                mainViewModel.fireAuthForAction(action, authEvent)
            },
            onNewAuthCooldown = settingsViewModel::onNewCooldown,
            onDeleteClick = settingsViewModel::deleteAll
        )
    }
}

@Composable
fun SettingsScreenUi(
    settingsState: SettingsViewModel.SettingsState,
    onAuthRequired: (Action, AuthEvent) -> Unit,
    onNewAuthCooldown: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
    ) {
        SettingsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAuthRequired(Action.ChangePin, AuthEvent.UpdatePin())
            },
            iconRes = R.drawable.lock_closed,
            text = stringResource(id = R.string.change_pin)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd),
                painter = painterResource(id = R.drawable.cheveron_right),
                contentDescription = "",
                tint = GreyDark
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LightGray
        )

        SettingsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onNewAuthCooldown()
            },
            iconRes = R.drawable.ic_clock,
            text = stringResource(id = R.string.auth_cooldown)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = stringResource(
                    id = R.string.sec,
                    settingsState.authCooldownSeconds.toString()
                ),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GreyDark
                )
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LightGray
        )

        SettingsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            },
            iconRes = R.drawable.ic_screen_capture,
            text = stringResource(id = R.string.block_screen)
        ) {
            Switch(
                modifier = Modifier.height(24.dp),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = BlueMain,
                    uncheckedTrackColor = GreyDark,
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent
                ),
                checked = !settingsState.screenCaptureEnabled,
                onCheckedChange = {
                    if (it) {
                        onAuthRequired(Action.TurnOffScreenCapture, AuthEvent.AuthenticateAction)
                    } else {
                        onAuthRequired(Action.TurnOnScreenCapture, AuthEvent.AuthenticateAction)
                    }
                }
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LightGray
        )

        SettingsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            },
            iconRes = R.drawable.ic_sharing,
            text = stringResource(id = R.string.block_sharing)
        ) {
            Switch(
                modifier = Modifier.height(24.dp),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = BlueMain,
                    uncheckedTrackColor = GreyDark,
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent
                ),
                checked = !settingsState.sharingEnabled,
                onCheckedChange = {
                    if (it) {
                        onAuthRequired(Action.TurnOffSharing, AuthEvent.AuthenticateAction)
                    } else {
                        onAuthRequired(Action.TurnOnSharing, AuthEvent.AuthenticateAction)
                    }
                })
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = LightGray
        )

        SettingsButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showDialog = true
            },
            iconRes = R.drawable.trash,
            textColor = RedError,
            iconColor = RedError,
            text = stringResource(id = R.string.delete_all)
        ) {}

        Dialog(
            modifier = Modifier.padding(horizontal = 50.dp),
            showDialog = showDialog,
            title = stringResource(id = R.string.delete_all),
            text = stringResource(id = R.string.delete_all_description),
            onDismiss = { showDialog = false },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { showDialog = false },
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(width = 1.dp, color = BlueMain),
                        colors = ButtonDefaults.buttonColors(containerColor = White)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            textAlign = TextAlign.Center,
                            color = BlueMain,
                            fontWeight = FontWeight(500),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onDeleteClick() },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueMain)
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            textAlign = TextAlign.Center,
                            color = White,
                            fontWeight = FontWeight(500),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview
fun PreviewSettings() {
    AANotesTheme {
        SettingsScreenUi(
            onAuthRequired = { _, _ ->

            },
            settingsState = SettingsViewModel.SettingsState(),
            onDeleteClick = {},
            onNewAuthCooldown = {})
    }
}