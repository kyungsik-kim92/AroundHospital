package com.example.domain.model

data class KakaoSearch(
    val documents: List<KakaoMapInfo>,
    val meta: KakaoSearchMeta
)

data class KakaoMapInfo(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
)

data class KakaoSearchMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)
