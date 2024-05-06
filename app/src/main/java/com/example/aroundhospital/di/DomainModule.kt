package com.example.aroundhospital.di

import com.example.aroundhospital.kakaomap.KakaoMapManager
import com.example.aroundhospital.kakaomap.KakaoMapManagerImpl
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