package com.example.domain.util.ext


import com.example.domain.exception.NetworkFailureException
import com.example.domain.exception.SearchErrorException
import retrofit2.Response

fun <T> Response<T>.mapTo(): T =
    if (isSuccessful) {
        body() ?: throw SearchErrorException("[${code()} - ${raw()}]")
    } else throw NetworkFailureException("[${code()} - ${raw()}]")