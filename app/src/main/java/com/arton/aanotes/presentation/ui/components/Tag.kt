package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.presentation.ui.theme.*

@Composable
fun Tag(
    tag: Tag,
    isSelected: Boolean = false,
    onTagClick: (Tag) -> Unit = {}
) {
    Box(modifier = Modifier
        .wrapContentWidth()
        .height(32.dp)
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
            .height(32.dp)
            .wrapContentWidth()
            .background(
                shape = RoundedCornerShape(100.dp),
                color = TagInactive
            )
    ) {
        CustomTextField(
            modifier = Modifier
                .align(Alignment.Center)
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

@Preview
@Composable
fun PreviewNewTag() {
    AANotesTheme {
        NewTag()
    }
}