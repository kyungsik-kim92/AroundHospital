package com.example.aroundhospital.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aroundhospital.response.Document

@Database(entities = [Document::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}