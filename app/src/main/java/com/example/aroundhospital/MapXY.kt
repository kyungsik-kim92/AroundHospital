package com.example.aroundhospital

import net.daum.mf.map.api.MapPoint

fun MapPoint.toMapXY() =
    MapXY(this.mapPointGeoCoord.longitude.toString(), this.mapPointGeoCoord.latitude.toString())

data class MapXY(
    val x: String = "",
    val y: String = ""
)