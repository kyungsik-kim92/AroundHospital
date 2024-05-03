package com.example.aroundhospital.data.remote


import com.example.aroundhospital.KakaoService
import com.example.aroundhospital.response.KakaoSearchResponse
import retrofit2.Response
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(private val kakaoService: KakaoService) :
    KakaoRemoteDataSource {


    override suspend fun getSearchHospital(x: String, y: String): Response<KakaoSearchResponse> =
        kakaoService.getSearchHospital(x = x, y = y)
}