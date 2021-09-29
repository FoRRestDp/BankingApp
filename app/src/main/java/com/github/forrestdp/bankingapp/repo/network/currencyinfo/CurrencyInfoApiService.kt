package com.github.forrestdp.bankingapp.repo.network.currencyinfo

import com.github.forrestdp.bankingapp.repo.model.currencyinfo.CurrencyInfo
import retrofit2.http.GET

interface CurrencyInfoApiService {
    @GET("daily_json.js")
    suspend fun getCurrencyInfo(): CurrencyInfo
}