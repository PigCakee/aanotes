package com.arton.aanotes.presentation.ui.screen.editor

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arton.aanotes.presentation.ui.AANotesAppState
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.EditorAppBar
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
                
            )
        }
    ) {

    }
}