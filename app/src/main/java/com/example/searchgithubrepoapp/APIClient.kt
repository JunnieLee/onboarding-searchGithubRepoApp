package com.example.searchgithubrepoapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private const val BASE_URL = "https://api.github.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization","Bearer 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") // 공통 header를 이런식으로 설정 가능
                .build()
            it.proceed(request)
        }.build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}