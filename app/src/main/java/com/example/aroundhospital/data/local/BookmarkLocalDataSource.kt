package com.example.aroundhospital.data.local

import com.example.aroundhospital.response.Document
import kotlinx.coroutines.flow.Flow

interface BookmarkLocalDataSource {

    val getAll: Flow<List<Document>>

    suspend fun delete(entity: Document)

    suspend fun insert(entity: Document)
}