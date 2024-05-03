package com.example.aroundhospital

import com.example.aroundhospital.exception.NetworkFailureException
import com.example.aroundhospital.exception.SearchErrorException
import retrofit2.Response

fun <T> Response<T>.mapTo(): T =
    if (isSuccessful) {
        body() ?: throw SearchErrorException("[${code()} - ${raw()}]")
    } else throw NetworkFailureException("[${code()} - ${raw()}]")