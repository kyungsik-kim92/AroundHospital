package com.example.data.api

import com.example.data.api.response.KakaoSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoService {
    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v2/local/search/category")
    suspend fun getSearchHospital(
        @Query("category_group_code") category_group_code: String = HOSPITAL_GROUP_CODE,
        @Query("x") x: String, //longitude
        @Query("y") y: String, //latitude
    ): KakaoSearchResponse


    companion object {
        fun create(): KakaoService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KakaoService::class.java)

        private const val HOSPITAL_GROUP_CODE = "HP8"
        private const val BASE_URL = "https://dapi.kakao.com/"
        private const val API_KEY = "a9c9b825ab60749e299b375dc7b313e5"
    }

}