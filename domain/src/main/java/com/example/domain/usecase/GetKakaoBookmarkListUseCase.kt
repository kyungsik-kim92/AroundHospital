package com.example.domain.usecase

import com.example.domain.repo.KakaoBookmarkRepository
import javax.inject.Inject

class GetKakaoBookmarkListUseCase @Inject constructor(private val kakaoBookmarkRepository: KakaoBookmarkRepository) {

    operator fun invoke() = kakaoBookmarkRepository.getAll
}