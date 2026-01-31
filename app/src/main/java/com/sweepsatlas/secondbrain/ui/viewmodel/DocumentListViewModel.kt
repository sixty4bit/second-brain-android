package com.sweepsatlas.secondbrain.ui.viewmodel

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

data class DocumentListState(
    val isLoading: Boolean = true,
    val folders: Map<String, List<Document>> = emptyMap(),
    val searchQuery: String = "",
    val searchResults: List<Document>? = null,
    val error: String? = null
)

@HiltViewModel
class DocumentListViewModel @Inject constructor(
    private val repository: DocumentRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DocumentListState())
    val state: StateFlow<DocumentListState> = _state.asStateFlow()
    
    init {
        loadDocuments()
    }
    
    fun loadDocuments() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            repository.getDocumentsByFolder()
                .onSuccess { folders ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        folders = folders
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load documents"
                    )
                }
        }
    }
    
    fun search(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        
        if (query.isBlank()) {
            _state.value = _state.value.copy(searchResults = null)
            return
        }
        
        viewModelScope.launch {
            repository.searchDocuments(query)
                .onSuccess { results ->
                    _state.value = _state.value.copy(searchResults = results)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        error = e.message ?: "Search failed"
                    )
                }
        }
    }
    
    fun clearSearch() {
        _state.value = _state.value.copy(
            searchQuery = "",
            searchResults = null
        )
    }
}
