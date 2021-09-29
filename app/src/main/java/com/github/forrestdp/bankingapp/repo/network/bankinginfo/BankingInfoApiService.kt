package com.github.forrestdp.bankingapp.repo.network.bankinginfo

import com.github.forrestdp.bankingapp.repo.model.bankinginfo.BankingInfo
import retrofit2.http.GET

interface BankingInfoApiService {
    @GET("test/android/v1/users.json")
    suspend fun getBankingInfo(): BankingInfo
}