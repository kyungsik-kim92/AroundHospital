package com.example.data.di


import com.example.data.repo.KakaoBookmarkRepositoryImpl
import com.example.data.repo.KakaoRepositoryImpl
import com.example.data.source.local.BookmarkLocalDataSource
import com.example.data.source.local.BookmarkLocalDataSourceImpl
import com.example.data.source.remote.KakaoRemoteDataSource
import com.example.data.source.remote.KakaoRemoteDataSourceImpl
import com.example.domain.repo.KakaoBookmarkRepository
import com.example.domain.repo.KakaoRepository
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
        bookmarkRepositoryImpl: KakaoBookmarkRepositoryImpl
    ): KakaoBookmarkRepository


    @Binds
    @Singleton
    abstract fun provideKakaoRemoteDataSource(kakaoRemoteDataSourceImpl: KakaoRemoteDataSourceImpl): KakaoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBookmarkLocalDataSource(
        bookmarkLocalDataSourceImpl: BookmarkLocalDataSourceImpl
    ): BookmarkLocalDataSource

}