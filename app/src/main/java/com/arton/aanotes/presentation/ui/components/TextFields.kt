package com.arton.aanotes.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arton.aanotes.common.CORNERS_RADIUS_10
import com.arton.aanotes.common.TEXT_FIELDS_WITH_FRACTION
import com.arton.aanotes.presentation.ui.theme.*

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