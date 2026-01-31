package com.sweepsatlas.secondbrain.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweepsatlas.secondbrain.data.model.Document
import com.sweepsatlas.secondbrain.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocumentDetailState(
    val isLoading: Boolean = true,
    val document: Document? = null,
    val error: String? = null
)

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    private val repository: DocumentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val folder: String = checkNotNull(savedStateHandle["folder"])
    private val slug: String = checkNotNull(savedStateHandle["slug"])
    
    private val _state = MutableStateFlow(DocumentDetailState())
    val state: StateFlow<DocumentDetailState> = _state.asStateFlow()
    
    init {
        loadDocument()
    }
    
    private fun loadDocument() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            repository.getDocument(folder, slug)
                .onSuccess { document ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        document = document
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load document"
                    )
                }
        }
    }
    
    fun refresh() {
        loadDocument()
    }
}
