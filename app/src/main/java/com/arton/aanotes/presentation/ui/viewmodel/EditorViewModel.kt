package com.arton.aanotes.presentation.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arton.aanotes.domain.entity.Note
import com.arton.aanotes.domain.entity.Tag
import com.arton.aanotes.domain.repo.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private var editorStateFlow = MutableStateFlow(EditorState())
    val editorState = editorStateFlow.asStateFlow()

    private var currentNote: Note? = null

    private var insertTimer = object : CountDownTimer(1000L, 1000L) {
        override fun onTick(p0: Long) {
        }

        override fun onFinish() {
            viewModelScope.launch {
                currentNote?.let { notesRepository.insertNote(it) }
                editorStateFlow.update { editorState -> editorState.copy(isSaving = false, currentNote = currentNote) }
            }
        }
    }

    private val combine = combine(
        notesRepository.tags,
        notesRepository.currentNoteId,
        notesRepository.isSharingEnabled
    ) { tags, id, sharingEnabled ->
        currentNote = if (id == null) {
            val newNote = Note(
                id = System.currentTimeMillis(),
                createdAt = Date(System.currentTimeMillis()),
                editedAt = Date(System.currentTimeMillis())
            )
            notesRepository.setCurrentNote(newNote)
            newNote
        } else {
            notesRepository.getNotes().firstOrNull { it.id == id }?.mapToEntity()
        }
        editorStateFlow.update { editorState ->
            editorState.copy(
                currentNote = currentNote,
                isSaving = false,
                isSharingEnabled = sharingEnabled,
                availableTags = tags,
            )
        }
    }

    init {
        viewModelScope.launch {
            combine.collect()
        }
    }

    fun onTitleChanged(title: String) = viewModelScope.launch {
        currentNote = currentNote?.copy(title = title, editedAt = Date(System.currentTimeMillis()))
        insertTimer.cancel()
        insertTimer.start()
        editorStateFlow.update { editorState -> editorState.copy(isSaving = true) }
    }

    fun onBodyChanged(body: String) = viewModelScope.launch {
        currentNote = currentNote?.copy(body = body, editedAt = Date(System.currentTimeMillis()))
        insertTimer.cancel()
        insertTimer.start()
        editorStateFlow.update { editorState -> editorState.copy(isSaving = true) }
    }

    fun onTagAdded(tag: Tag) = viewModelScope.launch {
        notesRepository.insertTag(tag)
    }

    fun onTagRemoved(tag: Tag) = viewModelScope.launch {
        notesRepository.deleteTag(tag)
    }

    fun onTagCreated(tag: Tag) {
        onTagAdded(tag)
    }
}

data class EditorState(
    val isSaving: Boolean = false,
    val isSharingEnabled: Boolean = false,
    val currentNote: Note? = null,
    val availableTags: List<Tag> = listOf()
)