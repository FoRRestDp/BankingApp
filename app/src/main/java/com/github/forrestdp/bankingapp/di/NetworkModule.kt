package com.github.forrestdp.bankingapp.di

import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BASE_URL
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BankingInfoApiService
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BigDecimalAdapter
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.CardHoldersApi
import com.github.forrestdp.bankingapp.repo.network.currencyinfo.CurrencyInfoApi
import com.github.forrestdp.bankingapp.repo.network.currencyinfo.CurrencyInfoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBankingInfoApiService(): BankingInfoApiService =
        CardHoldersApi.retrofitService

    @Provides
    @Singleton
    fun provideCurrencyInfoApiService(): CurrencyInfoApiService =
        CurrencyInfoApi.retrofitService
}