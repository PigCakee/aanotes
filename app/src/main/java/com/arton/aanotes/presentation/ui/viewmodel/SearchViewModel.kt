package com.arton.aanotes.presentation.ui.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.R
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.repo.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val searchStateFlow = MutableStateFlow(SearchState())
    val searchState = searchStateFlow.asStateFlow()

    private var query: String = ""

    private var job: Job? = null

    private val notesTags = combine(notesRepository.notes, notesRepository.tags) { notes, tags ->
        searchStateFlow.update { searchState ->
            val results = notes.filter { it.tags.containsAll(searchState.selectedTags) }
            searchState.copy(
                searchResults = SearchResults(
                    orderedByLastEdit = results,
                    orderedByDate = results.sortedByDescending { it.createdAt },
                    orderedAlphabetically = results.sortedBy { it.title }
                ),
                error = if (results.isEmpty()) R.string.empty_results else null,
                tags = tags
            )
        }
    }

    fun openNote(note: Note) {
        viewModelScope.launch {
            notesRepository.setCurrentNote(note)
            searchStateFlow.update { searchState ->
                searchState.copy(noteToOpen = note)
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    fun restoreNote(note: Note) {
        viewModelScope.launch {
            notesRepository.insertNote(note)
        }
    }

    init {
        viewModelScope.launch {
            notesTags.collect()
        }
    }

    fun onTagClick(tag: Tag) {
        searchStateFlow.update { searchState ->
            val selTags = searchState.selectedTags.toMutableList()
            if (selTags.contains(tag)) {
                selTags.remove(tag)
            } else {
                selTags.add(tag)
            }
            searchState.copy(selectedTags = selTags)
        }
        getNotes()
    }

    fun getNotes(query: String = this.query) {
        this.query = query.trim()
        job?.cancel()
        job = viewModelScope.launch {
            searchStateFlow.update { searchState ->
                val notes = notesRepository.getNotes(query).first()
                val results = notes.filter { it.tags.containsAll(searchState.selectedTags) }.map { it.mapToEntity() }
                searchState.copy(
                    searchResults = SearchResults(
                        orderedByLastEdit = results,
                        orderedByDate = results.sortedByDescending { it.createdAt },
                        orderedAlphabetically = results.sortedBy { it.title }
                    ),
                    error = if (results.isEmpty()) R.string.empty_results else null,
                )
            }
        }
    }

    fun createNewNote() {
        viewModelScope.launch {
            val note = Note(
                id = System.currentTimeMillis(),
                createdAt = Date(System.currentTimeMillis()),
                editedAt = Date(System.currentTimeMillis())
            )
            notesRepository.setCurrentNote(note)
            searchStateFlow.update { searchState ->
                searchState.copy(noteToOpen = note)
            }
        }
    }

    data class SearchState(
        val query: String = "",
        val tags: List<Tag> = listOf(),
        val selectedTags: List<Tag> = listOf(),
        val searchResults: SearchResults = SearchResults(),
        val noteToOpen: Note? = null,
        @StringRes val error: Int? = null
    )

    data class SearchResults(
        val orderedByLastEdit: List<Note> = listOf(),
        val orderedByDate: List<Note> = listOf(),
        val orderedAlphabetically: List<Note> = listOf()
    )
}