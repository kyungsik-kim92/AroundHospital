package com.example.aroundhospital.data.repo


import com.example.aroundhospital.data.local.BookmarkLocalDataSource
import com.example.data.api.response.Document
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(private val bookmarkLocalDataSource: BookmarkLocalDataSource) :
    BookmarkRepository {
    override val getAll: Flow<List<Document>>
        get() = bookmarkLocalDataSource.getAll

    override suspend fun delete(entity: Document) {
        bookmarkLocalDataSource.delete(entity)
    }

    override suspend fun insert(entity: Document) {
        bookmarkLocalDataSource.insert(entity)
    }
}