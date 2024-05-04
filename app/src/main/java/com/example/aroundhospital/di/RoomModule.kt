package com.example.aroundhospital.di

import android.content.Context
import androidx.room.Room
import com.example.aroundhospital.room.BookmarkDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Provides
    @Singleton
    fun provideBookmarkDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        klass = BookmarkDatabase::class.java,
        "book"
    ).build()

    @Provides
    @Singleton
    fun provideBookmarkDao(
        bookmarkDatabase: BookmarkDatabase
    ) = bookmarkDatabase.bookmarkDao()

}