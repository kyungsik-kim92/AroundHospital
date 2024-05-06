package com.example.domain.usecase

import com.example.domain.model.KakaoMapInfo
import com.example.domain.repo.KakaoBookmarkRepository
import javax.inject.Inject

class DeleteKakaoBookmarkUseCase @Inject constructor(private val kakaoBookmarkRepository: KakaoBookmarkRepository) {

    suspend operator fun invoke(item: KakaoMapInfo) {
        kakaoBookmarkRepository.delete(item)
    }

}