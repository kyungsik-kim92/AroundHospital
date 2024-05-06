package com.example.presenter.kakaomap

import android.location.Location
import com.example.domain.model.KakaoMapInfo
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions

fun Location.toLatLng() =
   LatLng.from(latitude, longitude)

fun KakaoMapInfo.toKakaoMapLabelOption(): LabelOptions =
    LabelOptions.from(id, LatLng.from(y.toDouble(), x.toDouble())).setTag(this)