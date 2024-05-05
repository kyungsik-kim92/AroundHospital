package com.example.data.source.local

import com.example.data.room.BookmarkDao
import com.example.domain.model.KakaoMapInfo
import com.example.domain.source.local.BookmarkLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkLocalDataSourceImpl @Inject constructor(private val bookmarkDao: BookmarkDao) :
    BookmarkLocalDataSource {
    override val getAll: Flow<List<KakaoMapInfo>>
        = bookmarkDao.getAll()

    override suspend fun delete(entity: KakaoMapInfo) {

         bookmarkDao.delete(entity)
    }

    override suspend fun insert(entity: KakaoMapInfo) {
        TODO("Not yet implemented")
    }

}