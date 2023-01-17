package com.arton.aanotes.presentation.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.utils.SnackbarManager
import com.arton.aanotes.data.entity.Note
import com.arton.aanotes.data.entity.Tag
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.NoteSearchItem
import com.arton.aanotes.presentation.ui.components.SearchAppBar
import com.arton.aanotes.presentation.ui.components.Tag
import com.arton.aanotes.presentation.ui.theme.Black
import com.arton.aanotes.presentation.ui.theme.BlueMain
import com.arton.aanotes.presentation.ui.theme.GreyDark
import com.arton.aanotes.presentation.ui.viewmodel.SearchViewModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onNewNoteCreated: () -> Unit = {}
) {

    val searchState by searchViewModel.searchState.collectAsState()

    if (searchState.noteToOpen != null) {
        onNewNoteCreated()
    }

    AANotesScaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            SearchAppBar(
                onQueryChanged = searchViewModel::getNotes
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = searchViewModel::createNewNote,
                shape = CircleShape,
                containerColor = BlueMain,
                content = { Icon(imageVector = Icons.Filled.Create, "", tint = Color.White) }
            )
        }
    ) {
        val deleteMessage = stringResource(id = com.arton.aanotes.R.string.note_deleted)
        SearchScreenUi(
            searchState = searchState,
            onTagClick = searchViewModel::onTagClick,
            onNoteClick = searchViewModel::openNote,
            onNoteDelete = {
                searchViewModel.deleteNote(it)
                SnackbarManager.showMessage(
                    messageText = deleteMessage,
                    action = {
                        searchViewModel.restoreNote(it)
                    }
                )
            }
        )
    }
}

@Composable
fun SearchScreenUi(
    searchState: SearchViewModel.SearchState,
    onTagClick: (Tag) -> Unit = {},
    onNoteClick: (Note) -> Unit = {},
    onNoteDelete: (Note) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .wrapContentHeight(),
            crossAxisSpacing = 10.dp,
            mainAxisSpacing = 12.dp
        ) {
            repeat(searchState.tags.size) { index ->
                val tag = searchState.tags[index]
                val isSelected = searchState.selectedTags.contains(tag)
                Tag(tag = tag, isSelected = isSelected, onTagClick = onTagClick)
            }
        }

        AnimatedVisibility(
            visible = searchState.error != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val errorIfQuery = stringResource(
                    id = com.arton.aanotes.R.string.empty_results,
                    searchState.query
                )
                val errorIfEmpty = stringResource(
                    id = com.arton.aanotes.R.string.empty_results_empty
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = if (searchState.query.isBlank()) errorIfEmpty else errorIfQuery,
                    color = Black,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }

        AnimatedVisibility(
            visible = searchState.error == null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                AnimatedVisibility(visible = searchState.searchResults.orderedByLastEdit.isNotEmpty()) {
                    NotesRow(
                        rowTitle = stringResource(id = com.arton.aanotes.R.string.order_last_edit),
                        notes = searchState.searchResults.orderedByLastEdit,
                        onNoteClick = onNoteClick,
                        onNoteDelete = onNoteDelete
                    )
                }
                AnimatedVisibility(visible = searchState.searchResults.orderedByDate.isNotEmpty()) {
                    NotesRow(
                        rowTitle = stringResource(id = com.arton.aanotes.R.string.order_date),
                        notes = searchState.searchResults.orderedByDate,
                        onNoteClick = onNoteClick,
                        onNoteDelete = onNoteDelete
                    )
                }
                AnimatedVisibility(visible = searchState.searchResults.orderedAlphabetically.isNotEmpty()) {
                    NotesRow(
                        rowTitle = stringResource(id = com.arton.aanotes.R.string.order_alphabet),
                        notes = searchState.searchResults.orderedAlphabetically,
                        onNoteClick = onNoteClick,
                        onNoteDelete = onNoteDelete
                    )
                }
            }
        }
    }
}

@Composable
fun NotesRow(
    rowTitle: String,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit = {},
    onNoteDelete: (Note) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = rowTitle,
            color = GreyDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .fillMaxWidth()
                .height(1.dp), color = GreyDark
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentPadding = PaddingValues(12.dp),
        ) {
            itemsIndexed(notes) { _, note ->
                NoteSearchItem(
                    note = note,
                    onNoteClick = onNoteClick,
                    onDeleteNote = onNoteDelete
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {

}