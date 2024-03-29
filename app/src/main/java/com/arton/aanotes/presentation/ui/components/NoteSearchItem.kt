package com.arton.aanotes.presentation.ui.components

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.utils.formatDateShort
import com.arton.aanotes.data.entity.Note
import com.arton.aanotes.presentation.ui.theme.*
import java.util.*

@Suppress("DEPRECATION")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteSearchItem(
    modifier: Modifier = Modifier,
    searchQuery: String,
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
    val vibratorManager = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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
                            vibratorManager.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            longPress = true
                        }
                    )
                    .then(surfaceModifier)
            ) {
                val titleSpan = buildAnnotatedString {
                    if (searchQuery.isNotBlank() && note.title.contains(searchQuery)) {
                        withStyle(
                            style = SpanStyle(color = BlueMain),
                        ) {
                            val before = note.title.substringBefore(searchQuery)
                            append(before.substring(before.lastIndexOf(" ") + 1))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = YellowHighlight,
                                background = YellowHighlightBackground
                            ),
                        ) {
                            append(searchQuery)
                        }
                        withStyle(
                            style = SpanStyle(color = BlueMain),
                        ) {
                            append(note.title.substringAfter(searchQuery))
                        }
                    } else {
                        append(note.title)
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .defaultMinSize(minHeight = 40.dp),
                    text = titleSpan,
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
                val bodySpan = buildAnnotatedString {
                    if (searchQuery.isNotBlank() && note.body.contains(searchQuery)) {
                        withStyle(
                            style = SpanStyle(color = BlueMain),
                        ) {
                            val before = note.body.substringBefore(searchQuery)
                            append(before.substring(before.lastIndexOf(" ") + 1))
                        }
                        withStyle(
                            style = SpanStyle(
                                color = YellowHighlight,
                                background = YellowHighlightBackground
                            ),
                        ) {
                            append(searchQuery)
                        }
                        withStyle(
                            style = SpanStyle(color = BlueMain),
                        ) {
                            append(note.body.substringAfter(searchQuery))
                        }
                    } else {
                        append(note.body)
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                        .defaultMinSize(minHeight = 60.dp),
                    text = bodySpan,
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
            ),
            searchQuery = "st"
        )
    }
}