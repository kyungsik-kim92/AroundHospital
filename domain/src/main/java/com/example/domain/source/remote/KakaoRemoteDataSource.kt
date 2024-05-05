package com.example.domain.source.remote

import com.example.domain.model.KakaoSearch

interface KakaoRemoteDataSource {

    suspend fun getSearchHospital(
        x: String, //longitude
        y: String, //latitude
    ): KakaoSearch
}