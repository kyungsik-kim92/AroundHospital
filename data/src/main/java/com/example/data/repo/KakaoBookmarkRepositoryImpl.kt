package com.example.data.repo

import com.example.data.mapper.toDocument
import com.example.data.mapper.toKakaoMapInfo
import com.example.data.source.local.BookmarkLocalDataSource
import com.example.domain.model.KakaoMapInfo
import com.example.domain.repo.KakaoBookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KakaoBookmarkRepositoryImpl @Inject constructor(private val bookmarkLocalDataSource: BookmarkLocalDataSource) :
    KakaoBookmarkRepository {
    override val getAll: Flow<List<KakaoMapInfo>>
        get() = bookmarkLocalDataSource.getAll.map { list -> list.map { item -> item.toKakaoMapInfo() } }

    override suspend fun delete(entity: KakaoMapInfo) {
        bookmarkLocalDataSource.delete(entity.toDocument())
    }

    override suspend fun insert(entity: KakaoMapInfo) {
        bookmarkLocalDataSource.insert(entity.toDocument())
    }
}
