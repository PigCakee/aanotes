package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.GreyDark
import com.google.relay.compose.BoxScopeInstanceImpl.align

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