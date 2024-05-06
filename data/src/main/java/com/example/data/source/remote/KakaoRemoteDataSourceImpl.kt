package com.example.data.source.remote

import com.example.data.api.KakaoService
import com.example.data.api.response.KakaoSearchResponse
import com.example.data.mapper.toKakaoSearch
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(private val kakaoService: KakaoService) :
    KakaoRemoteDataSource {


    override suspend fun getSearchHospital(x: String, y: String): KakaoSearchResponse =
        kakaoService.getSearchHospital(x = x, y = y)
}