package com.arton.aanotes.presentation.ui.screen.editor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arton.aanotes.R
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.presentation.ui.AANotesAppState
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.EditorAppBar
import com.arton.aanotes.presentation.ui.viewmodel.EditorState
import com.arton.aanotes.presentation.ui.viewmodel.EditorViewModel

@Composable
fun EditorScreen(
    appState: AANotesAppState,
    editorViewModel: EditorViewModel,
) {

    val editorState = editorViewModel.editorState.collectAsState()

    AANotesScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            EditorAppBar(
                titleResId = if (editorState.value.currentNote != null) R.string.edit_note else R.string.new_note,
                isSharingEnabled = editorState.value.isSharingEnabled,
                isSaving = editorState.value.isSaving,
                onShareClick = {

                }
            )
        }
    ) {

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
    val titleHint = stringResource(id = R.string.title_hint)
    val bodyHint = stringResource(id = R.string.body_hint)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TextField(
            value = editorState.currentNote?.title?.ifBlank { titleHint } ?: titleHint,
            onValueChange = onTitleChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = editorState.currentNote?.body?.ifBlank { bodyHint } ?: bodyHint,
            onValueChange = onBodyChanged
        )

        Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 24.dp))

        // Tags grid
    }
}