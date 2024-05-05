package com.example.data.repo

import com.example.domain.model.KakaoMapInfo
import com.example.domain.repo.KakaoBookmarkRepository
import com.example.domain.source.local.BookmarkLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KakaoBookmarkRepositoryImpl @Inject constructor(private val bookmarkLocalDataSource: BookmarkLocalDataSource) :
    KakaoBookmarkRepository {
    override val getAll: Flow<List<KakaoMapInfo>>
        get() = bookmarkLocalDataSource.getAll

    override suspend fun delete(entity: KakaoMapInfo) {
       bookmarkLocalDataSource.delete(entity)
    }

    override suspend fun insert(entity: KakaoMapInfo) {
        bookmarkLocalDataSource.insert(entity)
    }
}
