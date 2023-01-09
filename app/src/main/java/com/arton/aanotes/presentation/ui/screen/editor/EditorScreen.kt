package com.arton.aanotes.presentation.ui.screen.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.R
import com.arton.aanotes.common.utils.formatDateShort
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.EditorAppBar
import com.arton.aanotes.presentation.ui.components.NewTag
import com.arton.aanotes.presentation.ui.components.Tag
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.Black
import com.arton.aanotes.presentation.ui.theme.GreyDark
import com.arton.aanotes.presentation.ui.theme.White
import com.arton.aanotes.presentation.ui.viewmodel.EditorState
import com.arton.aanotes.presentation.ui.viewmodel.EditorViewModel
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@Composable
fun EditorScreen(
    editorViewModel: EditorViewModel,
) {

    val editorState by editorViewModel.editorState.collectAsState()

    AANotesScaffold(modifier = Modifier.systemBarsPadding(), topBar = {
        EditorAppBar(titleResId = if (editorState.currentNote != null) R.string.edit_note else R.string.new_note,
            isSharingEnabled = editorState.isSharingEnabled,
            isSaving = editorState.isSaving,
            onShareClick = {

            })
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(10.dp))
            Editor(
                editorState = editorState,
                onTitleChanged = editorViewModel::onTitleChanged,
                onBodyChanged = editorViewModel::onBodyChanged,
                onNewTag = editorViewModel::onTagCreated,
                onTagPressed = editorViewModel::onTagClicked
            )
        }
    }
}

@Composable
fun Editor(
    editorState: EditorState,
    onTitleChanged: (String) -> Unit = {},
    onBodyChanged: (String) -> Unit = {},
    onTagPressed: (Tag) -> Unit = {},
    onNewTag: (Tag) -> Unit = {}
) {
    val focus = LocalFocusManager.current
    val focusRequester = FocusRequester()

    val titleHint = stringResource(id = R.string.title_hint)
    val bodyHint = stringResource(id = R.string.body_hint)
    var title by remember {
        mutableStateOf(editorState.currentNote?.title ?: "")
    }
    var body by remember {
        mutableStateOf(editorState.currentNote?.body ?: "")
    }

    LaunchedEffect(editorState.currentNote?.title, editorState.currentNote?.body) {
        title = editorState.currentNote?.title ?: ""
        body = editorState.currentNote?.body ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = title,
            placeholder = {
                Text(
                    text = titleHint,
                    color = if (title.isBlank()) GreyDark else Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            onValueChange = {
                title = it
                onTitleChanged(it)
            },
            textStyle = TextStyle(
                color = if (title.isBlank()) GreyDark else Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Black,
                backgroundColor = White,
                cursorColor = Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .focusRequester(focusRequester),
            value = body,
            placeholder = {
                Text(
                    text = bodyHint,
                    color = if (body.isBlank()) GreyDark else Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
            },
            onValueChange = {
                body = it
                onBodyChanged(it)
            },
            textStyle = TextStyle(
                color = if (body.isBlank()) GreyDark else Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Black,
                backgroundColor = White,
                cursorColor = Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            text = stringResource(
                id = R.string.created_at,
                formatDateShort(editorState.currentNote?.createdAt)
            ) + "\n" + stringResource(
                id = R.string.edited_at,
                formatDateShort(editorState.currentNote?.editedAt)
            ),
            style = TextStyle(
                color = GreyDark,
                fontSize = 12.sp,
                lineHeight = 20.sp
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .wrapContentHeight(),
            crossAxisSpacing = 10.dp,
            mainAxisSpacing = 12.dp
        ) {
            repeat(editorState.availableTags.size) { index ->
                val tag = editorState.availableTags[index]
                val isSelected =
                    editorState.currentNote?.tags?.firstOrNull { tag.name == it.name } != null
                Tag(tag = tag, isSelected = isSelected, onTagClick = onTagPressed)
            }
            NewTag(onTagNameEntered = {
                onNewTag(Tag(name = it))
                focus.clearFocus()
            })
        }
    }
}

@Preview
@Composable
fun PreviewEditor() {
    AANotesTheme {
        Surface {
            Editor(
                EditorState(
                    currentNote = Note(
                        0,
                        "Dentist appointment",
                        "Make an appointment for dentist at 17:30\n" +
                                "\n" +
                                "And then I told her I had an early dentist appointment, and I'm hiding on your couch till she leaves.\n" +
                                "\n" +
                                "One day you're living your ordinary life, you're planning to go to a party, you're taking your children to school, you're making a dentist appointment.\n",
                        Date(),
                        Date(),
                        tags = listOf(
                            Tag("Highly important"),
                            Tag("Very important tag"),
                        )
                    ),
                    availableTags = listOf(
                        Tag("Highly important"),
                        Tag("Very important tag"),
                        Tag("Tag"),
                        Tag("Important"),
                    )
                )
            )
        }
    }
}