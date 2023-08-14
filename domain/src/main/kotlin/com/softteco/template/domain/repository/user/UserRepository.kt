package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.user.*


typealias LoginResponse = ApiResponse<Boolean>
typealias RegisterResponse = ApiResponse<Boolean>

interface UserRepository {
    suspend fun login(userAuth: LoginAuthDto): LoginResponse

    suspend fun registration(user: CreateUserDto): RegisterResponse
}