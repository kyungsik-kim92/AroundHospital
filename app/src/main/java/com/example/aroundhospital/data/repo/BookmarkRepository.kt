package com.example.aroundhospital.data.repo


import com.example.data.api.response.Document
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    val getAll : Flow<List<Document>>
    suspend fun delete(entity: Document)

    suspend fun insert(entity: Document)
}
