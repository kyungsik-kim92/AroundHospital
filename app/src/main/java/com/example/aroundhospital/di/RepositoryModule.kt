package com.example.aroundhospital.di


import com.example.aroundhospital.data.local.BookmarkLocalDataSource
import com.example.aroundhospital.data.local.BookmarkLocalDataSourceImpl

import com.example.aroundhospital.data.repo.BookmarkRepository
import com.example.aroundhospital.data.repo.BookmarkRepositoryImpl
import com.example.data.repo.KakaoRepositoryImpl
import com.example.data.source.remote.KakaoRemoteDataSourceImpl
import com.example.domain.repo.KakaoRepository
import com.example.domain.source.remote.KakaoRemoteDataSource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun provideKakaoRepository(kakaoRepositoryImpl: KakaoRepositoryImpl): KakaoRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        bookmarkRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository


    @Binds
    @Singleton
    abstract fun provideKakaoRemoteDataSource(kakaoRemoteDataSourceImpl: KakaoRemoteDataSourceImpl): KakaoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkLocalDataSource(
        bookmarkLocalDataSourceImpl: BookmarkLocalDataSourceImpl
    ): BookmarkLocalDataSource

}