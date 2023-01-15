package com.arton.aanotes.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onQueryChanged: (String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(true) }
    val description = rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(true) {
        focus.value = true
        inputService?.showSoftwareKeyboard()
        focusRequester.requestFocus()
    }
    Surface(
        color = White,
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = BlueMain,
            backgroundColor = BlueMain.copy(alpha = Transparent50)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                value = description.value,
                singleLine = true,
                onValueChange = {
                    description.value = it
                    onQueryChanged(it)
                },
                placeholder = {
                    Text(
                        text = "Search...",
                        color = GreyDark
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, "", tint = GreyDark)
                },
                trailingIcon = {
                    if (description.value.isNotEmpty()) {
                        PaperIconButton(id = R.drawable.ic_x) {
                            description.value = ""
                            onQueryChanged("")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions { inputService?.hideSoftwareKeyboard() },
                textStyle = TextStyle(fontWeight = FontWeight.W400, fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focus.value != focusState.isFocused) {
                            focus.value = focusState.isFocused
                            if (!focusState.isFocused && focus.value) {
                                inputService?.hideSoftwareKeyboard()
                            }
                        }
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = SearchGrey,
                    textColor = Black,
                    cursorColor = BlueMain,
                    focusedTrailingIconColor = BlueMain,
                    errorTrailingIconColor = BlueMain,
                    unfocusedTrailingIconColor = BlueMain,
                    disabledTrailingIconColor = BlueMain,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun SearchPreview() {
    AANotesTheme {
        SearchAppBar()
    }
}

@Composable
fun PaperIconButton(
    @DrawableRes id: Int,
    enabled: Boolean = true,
    border: Boolean = false,
    tint: Color = BlueMain,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painterResource(id = id),
            contentDescription = null,
            tint = tint,
            modifier = if (border) Modifier.border(
                0.5.dp, MaterialTheme.colorScheme.primary, CircleShape
            ) else Modifier
        )
    }
}