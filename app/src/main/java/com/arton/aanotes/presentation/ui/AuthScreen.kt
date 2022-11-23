package com.arton.aanotes.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.activity.contract.AuthEvent
import com.arton.aanotes.presentation.ui.components.InputCodeBottomSheet
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.BlueMain
import com.arton.aanotes.presentation.ui.theme.White
import com.arton.aanotes.presentation.ui.viewmodel.AuthState
import com.arton.aanotes.presentation.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    canUseBiometrics: Boolean,
    onAuthSuccess: () -> Unit = {},
    onBiometricActionRequest: () -> Unit = {}
) {
    val authState = viewModel.authState.collectAsState()
    if (authState.value.isComplete) {
        onAuthSuccess()
    }
    val context = LocalContext.current
    AuthScreenUi(
        authState = authState.value,
        canUseBiometrics = canUseBiometrics,
        onPinEntered = {
            viewModel.onPinEntered(context, it)
        },
        onBiometricActionRequest = onBiometricActionRequest
    )
}

@Composable
fun AuthScreenUi(
    authState: AuthState,
    canUseBiometrics: Boolean = true,
    onPinEntered: (String) -> Boolean = { true },
    onBiometricActionRequest: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueMain)
    ) {
        val (title, description, image, bottomSheet) = createRefs()
        Text(
            text = stringResource(id = authState.authEvent.getTitleRes()),
            color = White,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(title) {
                    top.linkTo(parent.top, margin = 25.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Image(
            modifier = Modifier
                .wrapContentSize()
                .clickable(
                    onClick = { onBiometricActionRequest() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .constrainAs(image) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(description.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            painter = painterResource(id = if (canUseBiometrics) R.drawable.ic_fingerprint else R.drawable.ic_round_close_24),
            contentDescription = null
        )
        Text(
            text = stringResource(id = authState.error ?: authState.authEvent.getDescriptionRes(canUseBiometrics)),
            color = White,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(description) {
                    bottom.linkTo(bottomSheet.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        InputCodeBottomSheet(
            modifier = Modifier.constrainAs(bottomSheet) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onPinEntered = onPinEntered
        )
    }
}

@Preview
@Composable
fun PreviewAuth() {
    AANotesTheme {
        AuthScreenUi(canUseBiometrics = true, authState = AuthState(authEvent = AuthEvent.AuthenticateAction))
    }
}