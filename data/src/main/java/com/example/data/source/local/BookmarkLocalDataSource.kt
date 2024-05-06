package com.example.data.source.local

import com.example.data.api.response.Document
import com.example.domain.model.KakaoMapInfo
import kotlinx.coroutines.flow.Flow

interface BookmarkLocalDataSource {
    val getAll: Flow<List<Document>>

    suspend fun delete(entity: Document)

    suspend fun insert(entity: Document)

}