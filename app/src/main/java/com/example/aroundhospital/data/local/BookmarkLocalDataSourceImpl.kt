package com.example.aroundhospital.data.local

import com.example.data.api.response.Document
import com.example.data.room.BookmarkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkLocalDataSourceImpl @Inject constructor(private val bookmarkDao: BookmarkDao) :
    BookmarkLocalDataSource {

    override val getAll: Flow<List<Document>>
        get() = bookmarkDao.getAll()

    override suspend fun delete(entity: Document) {
        bookmarkDao.delete(entity)
    }

    override suspend fun insert(entity: Document) {
        bookmarkDao.insert(entity)
    }
}