package com.example.data.mapper

import com.example.data.api.response.Document
import com.example.data.api.response.KakaoSearchResponse
import com.example.data.api.response.Meta
import com.example.domain.model.KakaoMapInfo
import com.example.domain.model.KakaoSearch
import com.example.domain.model.KakaoSearchMeta


fun Document.toKakaoMapInfo() = KakaoMapInfo(
    address_name,
    category_group_code,
    category_group_name,
    category_name,
    distance,
    id,
    phone,
    place_name,
    place_url,
    road_address_name,
    x,
    y
)


fun Meta.toKakaoSearchMeta() = KakaoSearchMeta(
    is_end, pageable_count, same_name, total_count
)


fun KakaoSearchResponse.toKakaoSearch() = KakaoSearch(
    documents.map { it.toKakaoMapInfo() },
    meta.toKakaoSearchMeta()
)
