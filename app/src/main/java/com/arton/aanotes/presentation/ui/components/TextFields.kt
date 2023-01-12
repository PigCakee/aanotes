package com.arton.aanotes.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.CORNERS_RADIUS_10
import com.arton.aanotes.common.TEXT_FIELDS_WITH_FRACTION
import com.arton.aanotes.presentation.ui.theme.*
import com.google.relay.compose.BoxScopeInstanceImpl.align

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AANotesTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    @StringRes errorMessageId: Int,
    isValueError: Boolean,
    @StringRes placeholderId: Int,
    @StringRes textFieldNameId: Int,
    withFraction: Float = TEXT_FIELDS_WITH_FRACTION,
    named: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier) {
        AnimatedVisibility(visible = named) {
            Row() {
                Text(
                    text = stringResource(id = textFieldNameId),
                    style = TextStyle(fontWeight = FontWeight.W600)
                )
            }
            VerticalSpacer(5.dp)
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(withFraction),
            value = value,
            onValueChange = {
                onValueChanged(it)
            },
            placeholder = {
                Text(
                    text = stringResource(id = placeholderId),
                    color = SecondaryTextColor
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(CORNERS_RADIUS_10.dp),
            isError = isValueError,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = PrimaryTextColor,
                cursorColor = PrimaryTextColor,
                unfocusedIndicatorColor = LightGray,
                focusedIndicatorColor = IrisBlue.copy(alpha = Transparent70),
                errorIndicatorColor = MaterialTheme.colors.error,

                ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )
        AnimatedVisibility(visible = isValueError) {
            Text(
                text = stringResource(id = errorMessageId),
                color = MaterialTheme.colors.error.copy(alpha = 0.6f),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    placeholderText: String = "Placeholder",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    onValueChanged: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    BasicTextField(
        modifier = modifier.wrapContentWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(GreyDark),
        textStyle = LocalTextStyle.current.copy(
            color = GreyDark,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) Text(
                text = placeholderText,
                style = LocalTextStyle.current.copy(
                    color = GreyDark,
                    fontSize = fontSize
                )
            )
            innerTextField()
        },
        keyboardActions = KeyboardActions(onDone = {
            onValueChanged(text)
            text = ""
        })
    )
}

@Preview
@Composable
fun PreviewCustomTextField() {
    AANotesTheme {
        CustomTextField(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp)
                .wrapContentWidth()
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(100.dp)
                ),
            fontSize = 12.sp,
            placeholderText = "New tag +"
        ) {

        }
    }
}