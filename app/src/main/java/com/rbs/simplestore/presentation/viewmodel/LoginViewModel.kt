package com.rbs.simplestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.simplestore.data.local.DataStoreManager
import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val useCase: UserUseCase
) : ViewModel() {
    private val _loginEvent = MutableSharedFlow<Boolean>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private val _viewState = MutableSharedFlow<ResultState<Boolean>>()
    val viewState = _viewState.asSharedFlow()

    var username = ""
    var password = ""

    init {
        isLoggedIn()
    }

    private fun isLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.isLoggedIn.collectLatest { isLogin ->
                _loginEvent.emit(isLogin)
            }
        }
    }

    fun validate() {
        _isButtonEnabled.value = username.isNotBlank() && password.isNotBlank()
    }

    fun loginUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.emit(ResultState.Loading)
            val request = LoginRequest(username, password)

            when (val result = useCase.loginUser(request)) {
                is ResultState.Success -> {
                    dataStoreManager.apply {
                        saveLoginState(true)
                        saveUserId(result.data.id)
                    }
                    _viewState.emit(ResultState.Success(true))
                }

                is ResultState.Error -> _viewState.emit(ResultState.Error(result.message))
                else -> Unit
            }
        }
    }
}