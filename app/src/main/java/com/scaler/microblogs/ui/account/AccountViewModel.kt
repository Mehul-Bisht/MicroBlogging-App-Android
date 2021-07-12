package com.scaler.microblogs.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scaler.libconduit.requests.UserUpdateData
import com.scaler.libconduit.responses.UserResponse
import com.scaler.microblogs.data.AppRepositoryImpl
import com.scaler.microblogs.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountViewModel(
    private val service: AppRepositoryImpl
) : ViewModel() {

    private val _accountStatus: MutableStateFlow<AccountState> = MutableStateFlow(AccountState.Initial)
    val accountStatus: StateFlow<AccountState> get() = _accountStatus

    sealed class AccountState {
        object Initial: AccountState()
        object Loading: AccountState()
        data class Error(val message: String?): AccountState()
        data class Success(val data: UserResponse?): AccountState()
    }

    fun getAccount(token: String) {

        service.viewProfile(token)

        viewModelScope.launch {

            service.profileStatus.collect { state ->

                when(state) {

                    is Resource.Loading -> {

                        _accountStatus.value = AccountState.Loading
                    }
                    is Resource.Success -> {

                        _accountStatus.value = AccountState.Success(state.data)
                    }
                    is Resource.Error -> {

                        _accountStatus.value = AccountState.Error(state.message)
                    }
                }
            }
        }
    }

    fun updateAccount(user: UserUpdateData) {


    }
}