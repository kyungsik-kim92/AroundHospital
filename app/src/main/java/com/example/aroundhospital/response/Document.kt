package com.example.aroundhospital.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.daum.mf.map.api.MapPOIItem

@Entity(tableName = "book")
data class Document(
    @ColumnInfo(name = "address_name")
    val address_name: String,
    @ColumnInfo(name = "category_group_code")
    val category_group_code: String,
    @ColumnInfo(name = "category_group_name")
    val category_group_name: String,
    @ColumnInfo(name = "category_name")
    val category_name: String,
    @ColumnInfo(name = "distance")
    val distance: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "place_name")
    val place_name: String,
    @ColumnInfo(name = "place_url")
    val place_url: String,
    @ColumnInfo(name = "road_address_name")
    val road_address_name: String,
    @ColumnInfo(name = "x")
    val x: String,
    @ColumnInfo(name = "y")
    val y: String
)

fun Document.toMapPOIItem() : MapPOIItem {
    return MapPOIItem().apply {
        itemName = place_name
        mapPoint = net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())
        markerType = MapPOIItem.MarkerType.RedPin
        userObject = this@toMapPOIItem
    }
}