package com.scaler.microblogs.data

import com.scaler.libconduit.responses.UserResponse
import com.scaler.microblogs.utils.Resource

interface AuthRepository {

    fun signUp(username: String, email: String, password: String)

    fun login(email: String, password: String)
}