package com.example.domain.usecase


import com.example.domain.repo.KakaoRepository
import com.example.domain.util.ext.asResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHospitalsUseCase @Inject constructor(private val kakaoRepository: KakaoRepository) {
    operator fun invoke(x: String, y: String) = flow {
        emit(kakaoRepository.getSearchHospital(x, y))
    }.asResult()
}
