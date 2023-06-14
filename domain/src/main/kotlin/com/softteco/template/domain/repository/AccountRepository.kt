package com.softteco.template.domain.repository

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.ApiResponse

typealias RegisterDbResponse = ApiResponse<Boolean>
interface AccountRepository {
    suspend fun addAccount (account: Account): RegisterDbResponse
}