package com.example.data.repo

import com.example.data.mapper.toKakaoSearch
import com.example.domain.model.KakaoSearch
import com.example.domain.repo.KakaoRepository
import com.example.data.source.remote.KakaoRemoteDataSource
import javax.inject.Inject

class KakaoRepositoryImpl @Inject constructor(private val kakaoRemoteDataSource: KakaoRemoteDataSource) :
    KakaoRepository {
    override suspend fun getSearchHospital(x: String, y: String): KakaoSearch {
        return kakaoRemoteDataSource.getSearchHospital(x, y).toKakaoSearch()

    }

}


