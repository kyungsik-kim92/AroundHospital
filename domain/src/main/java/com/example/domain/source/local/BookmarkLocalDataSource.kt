package com.example.domain.source.local

import com.example.domain.model.KakaoMapInfo
import kotlinx.coroutines.flow.Flow

interface BookmarkLocalDataSource {
    val getAll: Flow<List<KakaoMapInfo>>

    suspend fun delete(entity: KakaoMapInfo)

    suspend fun insert(entity: KakaoMapInfo)

}