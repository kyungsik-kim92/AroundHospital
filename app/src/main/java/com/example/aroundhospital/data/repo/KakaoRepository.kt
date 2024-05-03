package com.example.aroundhospital.data.repo


import com.example.aroundhospital.response.KakaoSearchResponse
import retrofit2.Response

interface KakaoRepository {

    suspend fun getSearchHospital(
        x: String, //longitude
        y: String, //latitude
    ): Response<KakaoSearchResponse>
}