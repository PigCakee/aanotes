package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.*
import com.arton.aanotes.presentation.ui.theme.Cerulean
import com.arton.aanotes.presentation.ui.theme.White

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            if (!isLoading) {
                keyboardController?.hide()
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth(BUTTONS_WITH_FRACTION)
            .height(BUTTONS_HEIGHT.dp),
        shape = RoundedCornerShape(CORNERS_RADIUS_10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Cerulean,
            contentColor = MaterialTheme.colors.onSurface,
            disabledBackgroundColor = MaterialTheme.colors.primaryVariant,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = buttonText, fontSize = 14.sp, color = White)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RethinkOutlinedButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    color: Color = Cerulean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedButton(
        onClick = {
            if (!isLoading) {
                keyboardController?.hide()
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth(BUTTONS_WITH_FRACTION)
            .height(BUTTONS_HEIGHT.dp)
            .border(
                width = ONE.dp,
                color = color,
                shape = RoundedCornerShape(CORNERS_RADIUS_10.dp)
            ),
        shape = RoundedCornerShape(CORNERS_RADIUS_10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color
        )
    ) {
        Text(text = buttonText, fontSize = 14.sp, color = color)
    }
}