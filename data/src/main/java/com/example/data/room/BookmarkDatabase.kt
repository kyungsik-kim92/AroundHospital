package com.example.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.api.response.Document
import com.example.domain.model.KakaoMapInfo

@Database(entities = [KakaoMapInfo::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}