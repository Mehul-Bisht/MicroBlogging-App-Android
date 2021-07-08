package com.scaler.microblogs.data

import android.util.Log
import com.scaler.libconduit.apis.ConduitApi
import com.scaler.libconduit.requests.UserLoginData
import com.scaler.libconduit.requests.UserLoginRequest
import com.scaler.libconduit.requests.UserSignupData
import com.scaler.libconduit.requests.UserSignupRequest
import com.scaler.libconduit.responses.UserResponse
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Exception

class AuthRepositoryImpl(
    private val service: ConduitApi
): AuthRepository {

    private val _signUpStatus: MutableStateFlow<Resource<UserResponse>> = MutableStateFlow(Resource.Loading())
    val signUpStatus: StateFlow<Resource<UserResponse>> get() = _signUpStatus

    private val _loginStatus: MutableStateFlow<Resource<UserResponse>> = MutableStateFlow(Resource.Loading())
    val loginStatus: StateFlow<Resource<UserResponse>> get() = _loginStatus

    override fun signUp(username: String, email: String, password: String) {

        CoroutineScope(Dispatchers.IO).launch {

            if (username.isEmpty()) {

                _signUpStatus.value =  Resource.Error("please enter a valid username")
            }
            else if (email.isEmpty()) {

                _signUpStatus.value = Resource.Error("please enter a valid email")
            }
            else if (password.isEmpty()) {

                _signUpStatus.value = Resource.Error("please enter a valid password")
            }
            else if (password.length < 6) {

                _signUpStatus.value = Resource.Error("password is too short!")
            } else {

                try {

                    val response = service.registerUser(
                        UserSignupRequest(
                            UserSignupData(
                                username = username,
                                email = email,
                                password = password
                            )
                        )
                    )

                    _signUpStatus.value = Resource.Success(response)

                } catch (e: Exception) {

                    Log.d("signup ","""
                        $e
                    """.trimIndent())
                    _signUpStatus.value = Resource.Error("An unknown error occurred!")
                }
            }
        }
    }

    override fun login(email: String, password: String) {

        CoroutineScope(Dispatchers.IO).launch {

            if (email.isEmpty()) {

                _loginStatus.value = Resource.Error("please enter a valid email")
            }
            if (password.isEmpty()) {

                _loginStatus.value = Resource.Error("please enter a valid password")
            }
            if (password.length < 6) {

                _loginStatus.value = Resource.Error("password is too short!")
            } else {

                try {

                    val response = service.loginUser(
                        UserLoginRequest(
                            UserLoginData(
                                email = email,
                                password = password
                            )
                        )
                    )

                    _loginStatus.value = Resource.Success(response)

                } catch (e: Exception) {

                    Log.d("login ","""
                        $e
                    """.trimIndent())
                    _loginStatus.value = Resource.Error("An unknown error occurred!")
                }
            }
        }
    }
}