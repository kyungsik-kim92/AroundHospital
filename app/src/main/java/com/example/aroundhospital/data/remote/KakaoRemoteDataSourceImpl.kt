package com.example.hospitalfinder.data.source.remote


import com.example.aroundhospital.KakaoService
import com.example.aroundhospital.response.KakaoSearchResponse
import retrofit2.Response

class KakaoRemoteDataSourceImpl (private val kakaoService: KakaoService) :
    KakaoRemoteDataSource {


    override suspend fun getSearchHospital(x: String, y: String): Response<KakaoSearchResponse> =
        kakaoService.getSearchHospital(x = x, y = y)
}