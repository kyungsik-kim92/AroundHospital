package com.example.aroundhospital.data.repo


import com.example.aroundhospital.response.KakaoSearchResponse
import com.example.hospitalfinder.data.source.remote.KakaoRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class KakaoRepositoryImpl @Inject constructor(private val kakaoRemoteDataSource: KakaoRemoteDataSource) :
    KakaoRepository {

    override suspend fun getSearchHospital(x: String, y: String): Response<KakaoSearchResponse> =
        kakaoRemoteDataSource.getSearchHospital(x = x, y = y)
}