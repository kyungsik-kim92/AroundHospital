package com.example.data.source.remote

import com.example.data.api.KakaoService
import com.example.data.mapper.toKakaoSearch
import com.example.domain.model.KakaoSearch
import com.example.domain.source.remote.KakaoRemoteDataSource
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(private val kakaoService: KakaoService) :
    KakaoRemoteDataSource {


    override suspend fun getSearchHospital(x: String, y: String): KakaoSearch =
        kakaoService.getSearchHospital(x = x, y = y).toKakaoSearch()
}