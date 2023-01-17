package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.data.entity.Tag
import com.arton.aanotes.presentation.ui.theme.*

@Composable
fun Tag(
    tag: Tag,
    isSelected: Boolean = false,
    onTagClick: (Tag) -> Unit = {}
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(
                color = if (isSelected) TagActive else TagInactive
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                enabled = true,
                role = Role.Button,
                onClick = {
                    onTagClick(tag)
                }
            ),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 6.dp, horizontal = 12.dp),
            text = tag.name,
            fontSize = 12.sp,
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
                .padding(horizontal = 12.dp)
                .wrapContentWidth(),
            fontSize = 12.sp,
            placeholderText = "Create new tag +"
        ) {
            onTagNameEntered(it)
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