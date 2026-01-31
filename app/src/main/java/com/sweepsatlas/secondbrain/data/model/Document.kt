package com.sweepsatlas.secondbrain.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a document from the Second Brain API.
 */
data class Document(
    val folder: String,
    val slug: String,
    val title: String,
    val created: String?,
    val tags: List<String>,
    val path: String,
    val content: String? = null,
    @SerializedName("html_content")
    val htmlContent: String? = null
) {
    val folderEmoji: String
        get() = when (folder) {
            "concepts" -> "💡"
            "journals" -> "📝"
            "projects" -> "📁"
            else -> "📄"
        }
    
    val displayTitle: String
        get() = "$folderEmoji $title"
}

/**
 * API response wrappers.
 */
data class DocumentsResponse(
    val documents: List<Document>
)

data class FoldersResponse(
    val folders: Map<String, List<Document>>
)

data class DocumentResponse(
    val document: Document
)

data class SearchResponse(
    val query: String,
    val results: List<Document>
)
