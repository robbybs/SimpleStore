package com.rbs.simplestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.usecase.UserUseCase
import com.rbs.simplestore.presentation.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: UserUseCase
) : ViewModel() {
    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private val _registerEvent = MutableSharedFlow<Boolean>()
    val registerEvent = _registerEvent.asSharedFlow()

    var fullname = ""
    var username = ""
    var email = ""
    var password = ""

    fun validate() {
        _isButtonEnabled.value =
            fullname.isNotBlank() && username.isNotBlank() && email.isNotBlank() && password.isNotBlank()
    }

    fun registerUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RegisterRequest(
                name = fullname,
                username = username,
                email = email,
                password = password
            )

            when (val result = useCase.registerUser(request)) {
                is ResultState.Success -> _registerEvent.emit(result.data)
                is ResultState.Error -> ResultState.Error(result.message)
                else -> ResultState.Loading
            }
        }
    }
}