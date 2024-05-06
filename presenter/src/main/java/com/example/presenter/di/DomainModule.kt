package com.example.presenter.di


import com.example.presenter.kakaomap.KakaoMapManager
import com.example.presenter.kakaomap.KakaoMapManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DomainModule {

    @Binds
    abstract fun bindKakaoMapManager(kakaoMapManagerImpl: KakaoMapManagerImpl): KakaoMapManager
}