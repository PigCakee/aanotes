package com.arton.aanotes.presentation.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.presentation.ui.components.AANotesScaffold
import com.arton.aanotes.presentation.ui.components.SearchAppBar
import com.arton.aanotes.presentation.ui.components.Tag
import com.arton.aanotes.presentation.ui.theme.Black
import com.arton.aanotes.presentation.ui.theme.BlueMain
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
                content = { Icon(imageVector = Icons.Filled.Add, "", tint = Color.White) }
            )
        }
    ) {
        SearchScreenUi(
            searchState = searchState,
            onTagClick = searchViewModel::onTagClick
        )
    }
}

@Composable
fun SearchScreenUi(
    searchState: SearchViewModel.SearchState,
    onTagClick: (Tag) -> Unit = {}
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
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

        AnimatedVisibility(visible = searchState.error != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(
                        id = com.arton.aanotes.R.string.empty_results,
                        searchState.query
                    ),
                    color = Black
                )
            }
        }

        AnimatedVisibility(visible = searchState.error == null) {

        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {

}