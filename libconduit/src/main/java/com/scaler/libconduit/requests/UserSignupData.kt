package com.scaler.libconduit.requests


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSignupData(
    @Json(name = "username") val username: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "password") val password: String?
)