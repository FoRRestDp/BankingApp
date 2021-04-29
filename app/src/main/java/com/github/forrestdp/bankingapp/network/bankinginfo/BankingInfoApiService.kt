package com.github.forrestdp.bankingapp.network.bankinginfo

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://hr.peterpartner.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BankingInfoApiService {
    @GET("test/android/v1/users.json")
    suspend fun getBankingInfo(): BankingInfo
}

object CardHoldersApi {
    val retrofitService: BankingInfoApiService by lazy {
        retrofit.create(BankingInfoApiService::class.java)
    }
}