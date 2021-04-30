package com.github.forrestdp.bankingapp.repo.network.bankinginfo

import com.github.forrestdp.bankingapp.repo.model.bankinginfo.BankingInfo
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.math.BigDecimal

const val BASE_URL = "https://hr.peterpartner.net/"

object BigDecimalAdapter {
    @FromJson fun fromJson(json: String) = BigDecimal(json)
    @ToJson fun toJson(value: BigDecimal) = value.toString()
}

private val moshi = Moshi.Builder()
    .add(BigDecimalAdapter)
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