package com.arton.aanotes.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.data.DataStoreManager
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.repo.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    dataStoreManager: DataStoreManager
) : ViewModel() {

    private var editorStateFlow = MutableStateFlow(EditorState())
    val editorState = editorStateFlow.asStateFlow()

    private var currentNote: Note? = null

    private val combine = combine(
        notesRepository.notes,
        notesRepository.tags,
        dataStoreManager.currentNoteId,
        dataStoreManager.isSharingEnabled
    ) { notes, tags, id, sharingEnabled ->
        currentNote = notes.firstOrNull { it.id == id }
        editorStateFlow.update { editorState ->
            editorState.copy(
                currentNote = currentNote,
                isSaving = false,
                isSharingEnabled = sharingEnabled,
                availableTags = tags
            )
        }
    }

    init {
        viewModelScope.launch {
            combine.collect()
        }
    }

    fun onTitleChanged(title: String) {
        currentNote = currentNote?.copy(title = title)
        currentNote?.let { notesRepository.insertNote(it) }
    }

    fun onBodyChanged(body: String) {
        currentNote = currentNote?.copy(body = body)
        currentNote?.let { notesRepository.insertNote(it) }
    }

    fun onTagAdded(tag: Tag) {

    }

    fun onTagRemoved(tag: Tag) {

    }

    fun onTagCreated(tagName: String) {
        onTagAdded(Tag(tagName))
    }
}

data class EditorState(
    val isSaving: Boolean = false,
    val isSharingEnabled: Boolean = false,
    val currentNote: Note? = null,
    val availableTags: List<Tag> = listOf()
)