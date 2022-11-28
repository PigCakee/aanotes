package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.presentation.ui.theme.*

@Composable
fun Tag(
    tag: Tag,
    isSelected: Boolean = false,
    onTagClick: (Tag) -> Unit = {}
) {
    Box(modifier = Modifier
        .wrapContentSize()
        .background(
            shape = RoundedCornerShape(100.dp),
            color = if (isSelected) TagActive else TagInactive
        )
        .clickable {
            onTagClick(tag)
        }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 6.dp, horizontal = 12.dp),
            text = tag.name,
            color = if (isSelected) White else GreyDark
        )
    }
}

@Preview
@Composable
fun PreviewTag() {
    AANotesTheme {
        Tag(Tag("Custom tag"))
    }
}

@Composable
fun NewTag(
    onTagNameEntered: (String) -> Unit = {}
) {
    var tag by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(
                shape = RoundedCornerShape(100.dp),
                color = TagInactive
            )
    ) {
        TextField(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 6.dp, horizontal = 12.dp),
            value = tag,
            placeholder = { Text(text = stringResource(id = com.arton.aanotes.R.string.new_tag)) },
            onValueChange = {
                tag = it
            },
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                onTagNameEntered(tag)
            })
        )
    }
}

@Preview
@Composable
fun PreviewNewTag() {
    AANotesTheme {
        NewTag()
    }
}