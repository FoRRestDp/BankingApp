package com.github.forrestdp.bankingapp.di

import com.github.forrestdp.bankingapp.di.qualifiers.BankingInfoApi
import com.github.forrestdp.bankingapp.di.qualifiers.CurrencyInfoApi
import com.github.forrestdp.bankingapp.repo.network.BigDecimalAdapter
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BankingInfoApiService
import com.github.forrestdp.bankingapp.repo.network.currencyinfo.CurrencyInfoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BANKING_INFO_BASE_URL = "https://hr.peterpartner.net/"
private const val CURRENCY_INFO_BASE_URL = "https://www.cbr-xml-daily.ru/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @BankingInfoApi
    fun provideBankingInfoRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BANKING_INFO_BASE_URL)
        .build()

    @Provides
    @CurrencyInfoApi
    fun provideCurrencyInfoRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(CURRENCY_INFO_BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideBankingInfoApiService(
        @BankingInfoApi retrofit: Retrofit,
    ): BankingInfoApiService =
        retrofit.create(BankingInfoApiService::class.java)

    @Provides
    @Singleton
    fun provideCurrencyInfoApiService(
        @CurrencyInfoApi retrofit: Retrofit,
    ): CurrencyInfoApiService =
        retrofit.create(CurrencyInfoApiService::class.java)
}