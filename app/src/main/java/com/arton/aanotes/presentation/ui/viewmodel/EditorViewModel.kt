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
                currentNote?.let {
                    saveNote(it)
                }
                editorStateFlow.update { editorState ->
                    editorState.copy(
                        isSaving = false,
                        currentNote = currentNote
                    )
                }
            }
        }
    }

    private suspend fun saveNote(it: Note) {
        val title = it.title
        val body = it.body
        if (title.isBlank() && body.isBlank()) {
            return
        } else if (title.isBlank()) {
            val words = body.split(" ")
            val newTitle = words.take(3).joinToString(" ")
            it.title = newTitle
            notesRepository.insertNote(it)
        } else if (body.isBlank()) {
            notesRepository.insertNote(it)
        } else {
            notesRepository.insertNote(it)
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
            notesRepository.notes.first().firstOrNull { it.id == id }
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

    fun onTagClicked(tag: Tag) = viewModelScope.launch {
        val updatedTags = currentNote?.tags?.toMutableList()
        updatedTags?.let { tags ->
            if (tags.contains(tag)) {
                updatedTags.remove(tag)
            } else {
                updatedTags.add(tag)
            }
            currentNote =
                currentNote?.copy(tags = tags, editedAt = Date(System.currentTimeMillis()))
            currentNote?.let { saveNote(it) }
            editorStateFlow.update { editorState ->
                editorState.copy(
                    isSaving = false,
                    currentNote = currentNote
                )
            }
        }
    }

    fun onTagRemoved(tag: Tag) = viewModelScope.launch {
        notesRepository.deleteTag(tag)
    }

    fun onTagCreated(tag: Tag) = viewModelScope.launch {
        notesRepository.insertTag(tag)
    }
}

data class EditorState(
    val isSaving: Boolean = false,
    val isSharingEnabled: Boolean = false,
    val currentNote: Note? = null,
    val availableTags: List<Tag> = listOf()
)