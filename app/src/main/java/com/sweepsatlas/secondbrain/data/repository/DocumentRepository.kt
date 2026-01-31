package com.sweepsatlas.secondbrain.data.repository

import com.sweepsatlas.secondbrain.data.api.SecondBrainApi
import com.sweepsatlas.secondbrain.data.model.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for accessing Second Brain documents.
 */
@Singleton
class DocumentRepository @Inject constructor(
    private val api: SecondBrainApi
) {
    suspend fun getAllDocuments(): Result<List<Document>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllDocuments()
            Result.success(response.documents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getDocumentsByFolder(): Result<Map<String, List<Document>>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDocumentsByFolder()
            Result.success(response.folders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getDocument(folder: String, slug: String): Result<Document> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDocument(folder, slug)
            Result.success(response.document)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchDocuments(query: String): Result<List<Document>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchDocuments(query)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
