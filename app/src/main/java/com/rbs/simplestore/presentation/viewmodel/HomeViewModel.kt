package com.rbs.simplestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.simplestore.data.local.DataStoreManager
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.model.ProductDomain
import com.rbs.simplestore.domain.model.UserDomain
import com.rbs.simplestore.domain.usecase.ProductUseCase
import com.rbs.simplestore.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val userUseCase: UserUseCase,
    private val dataStore: DataStoreManager
) : ViewModel() {
    private var _userEvent = MutableSharedFlow<Int>()
    val userEvent = _userEvent.asSharedFlow()

    private var _productState =
        MutableStateFlow<ResultState<List<ProductDomain>>>(ResultState.Loading)
    val productState = _productState.asStateFlow()

    private var _userState = MutableSharedFlow<ResultState<UserDomain>>()
    val userState = _userState.asSharedFlow()

    private val _logoutEvent = MutableSharedFlow<Boolean>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        getUserID()
        getProducts()
    }

    private fun getUserID() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.getUserId.collect {
                if (it != null) _userEvent.emit(it)
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = productUseCase.getProducts()) {
                is ResultState.Success -> _productState.update { ResultState.Success(result.data) }
                is ResultState.Error -> _productState.update { ResultState.Error(result.message) }
                else -> ResultState.Loading
            }
        }
    }

    fun getUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _userState.emit(ResultState.Loading)
            when (val result = userUseCase.getDetailUser(id)) {
                is ResultState.Success -> _userState.emit(ResultState.Success(result.data))
                is ResultState.Error -> _userState.emit(ResultState.Error(result.message))
                else -> Unit
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.clearLoginState()
            _logoutEvent.emit(true)
        }
    }
}