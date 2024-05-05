package com.example.domain.repo

import com.example.domain.model.KakaoSearch

interface KakaoRepository {

    suspend fun getSearchHospital(
        x: String, //longitude
        y: String, //latitude
    ): KakaoSearch
}