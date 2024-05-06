package com.example.data.source.remote

import com.example.data.api.response.KakaoSearchResponse
import com.example.domain.model.KakaoSearch

interface KakaoRemoteDataSource {

    suspend fun getSearchHospital(
        x: String, //longitude
        y: String, //latitude
    ): KakaoSearchResponse
}