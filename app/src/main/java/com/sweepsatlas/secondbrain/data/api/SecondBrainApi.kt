package com.sweepsatlas.secondbrain.data.api

import com.sweepsatlas.secondbrain.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for the Second Brain Rails API.
 */
interface SecondBrainApi {
    
    @GET("documents")
    suspend fun getAllDocuments(): DocumentsResponse
    
    @GET("documents/folders")
    suspend fun getDocumentsByFolder(): FoldersResponse
    
    @GET("documents/{folder}/{slug}")
    suspend fun getDocument(
        @Path("folder") folder: String,
        @Path("slug") slug: String
    ): DocumentResponse
    
    @GET("documents/search")
    suspend fun searchDocuments(
        @Query("q") query: String
    ): SearchResponse
}
