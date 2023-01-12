package com.arton.aanotes.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.utils.formatDateShort
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.presentation.ui.theme.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteSearchItem(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit = {},
    onDeleteNote: (Note) -> Unit = {}
) {
    var longPress by remember {
        mutableStateOf(false)
    }
    val surfaceModifier = if (longPress) {
        Modifier.blur(8.dp)
    } else {
        Modifier
    }
    Box(modifier = Modifier.wrapContentSize()) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .width(128.dp),
            contentColor = White,
            elevation = 4.dp,
            border = BorderStroke(1.dp, BlueMain)
        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .combinedClickable(
                        enabled = !longPress,
                        onClick = {
                            onNoteClick(note)
                        },
                        onLongClick = {
                            longPress = true
                        }
                    )
                    .then(surfaceModifier)
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .defaultMinSize(minHeight = 40.dp),
                    text = note.title,
                    color = BlueMain,
                    style = TextStyle(fontSize = 16.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Divider(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 4.dp, bottom = 8.dp, end = 12.dp)
                        .width(104.dp)
                        .height(1.dp),
                    color = BlueMain
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                        .defaultMinSize(minHeight = 60.dp),
                    text = note.body,
                    color = Black,
                    maxLines = 5,
                    style = TextStyle(fontSize = 10.sp),
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(128.dp)
                        .background(BlueMain)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        text = formatDateShort(note.createdAt),
                        color = White,
                        style = TextStyle(fontSize = 10.sp)
                    )
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = longPress,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.matchParentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        onDeleteNote(note)
                        longPress = false
                    }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Delete,
                        "",
                        tint = RedError
                    )
                }
                IconButton(
                    onClick = {
                        longPress = false
                    }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Close,
                        "",
                        tint = GreyDark
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNote() {
    AANotesTheme {
        NoteSearchItem(
            note = Note(
                1,
                "Dentist appointment at doctor",
                "Create a mobile app UI Kit that provide a basic notes functionality but with some improvement. Create a mobile app UI Kit that provide a basic notes functionality but with some improvement.",
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis())
            )
        )
    }
}