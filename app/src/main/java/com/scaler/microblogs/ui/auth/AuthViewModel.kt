package com.scaler.microblogs.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scaler.libconduit.responses.UserResponse
import com.scaler.microblogs.data.AuthRepositoryImpl
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val service: AuthRepositoryImpl
): ViewModel() {

    private val _signUpProgress: MutableStateFlow<SignUpProgress> = MutableStateFlow(SignUpProgress.Initial)
    val signUpProgress: StateFlow<SignUpProgress> get() = _signUpProgress

    private val _loginProgress: MutableStateFlow<LogInProgress> = MutableStateFlow(LogInProgress.Initial)
    val loginProgress: StateFlow<LogInProgress> get() = _loginProgress

    private val _signUpStatus = Channel<SignUpState>()
    val signUpStatus = _signUpStatus.receiveAsFlow()

    private val _loginStatus = Channel<LogInState>()
    val loginStatus = _loginStatus.receiveAsFlow()

    sealed class SignUpState {
        data class Success(val response: UserResponse?): SignUpState()
    }

    sealed class LogInState {
        data class Success(val response: UserResponse?): LogInState()
    }

    sealed class SignUpProgress {
        object Initial: SignUpProgress()
        object Loading: SignUpProgress()
        data class Error(val message: String?): SignUpProgress()
        object Completed: SignUpProgress()
    }

    sealed class LogInProgress {
        object Initial: LogInProgress()
        object Loading: LogInProgress()
        data class Error(val message: String?): LogInProgress()
        object Completed: LogInProgress()
    }

    fun signUp(username: String, email: String, password: String) {

        service.signUp(username, email, password)

        viewModelScope.launch {

            service.signUpStatus.collect { status ->

                when(status) {

                    is Resource.Loading -> {

                        _signUpProgress.value = SignUpProgress.Loading
                    }

                    is Resource.Success -> {

                        _signUpProgress.value = SignUpProgress.Completed
                        _signUpStatus.send(SignUpState.Success(status.data))
                    }

                    is Resource.Error -> {

                        _signUpProgress.value = SignUpProgress.Error(status.message)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {

        service.login(email, password)

        viewModelScope.launch {

            service.loginStatus.collect { status ->

                when(status) {

                    is Resource.Loading -> {

                        _loginProgress.value = LogInProgress.Loading
                    }

                    is Resource.Success -> {

                        _loginProgress.value = LogInProgress.Completed
                        _loginStatus.send(LogInState.Success(status.data))
                    }

                    is Resource.Error -> {

                        _loginProgress.value = LogInProgress.Error(status.message)
                    }
                }
            }
        }
    }
}