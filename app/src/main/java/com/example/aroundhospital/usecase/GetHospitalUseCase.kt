package com.example.aroundhospital.usecase

import com.example.aroundhospital.asResult
import com.example.aroundhospital.data.repo.KakaoRepository
import com.example.aroundhospital.mapTo
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHospitalsUseCase @Inject constructor(private val kakaoRepository: KakaoRepository) {
    operator fun invoke(x: String, y: String) = flow {
        emit(kakaoRepository.getSearchHospital(x, y).mapTo().documents)
    }.asResult()
}
