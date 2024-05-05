package com.example.domain.repo

import com.example.domain.model.KakaoMapInfo
import kotlinx.coroutines.flow.Flow

interface KakaoBookmarkRepository {

    val getAll : Flow<List<KakaoMapInfo>>
    suspend fun delete(entity: KakaoMapInfo)

    suspend fun insert(entity: KakaoMapInfo)
}
