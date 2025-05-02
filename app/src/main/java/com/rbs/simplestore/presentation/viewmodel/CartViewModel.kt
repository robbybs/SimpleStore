package com.rbs.simplestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.usecase.ProductUseCase
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
class CartViewModel @Inject constructor(
    private val useCase: ProductUseCase
) : ViewModel() {
    private var _viewState = MutableStateFlow<ResultState<List<CartDomain>>>(ResultState.Loading)
    val viewState = _viewState.asStateFlow()

    private var _deleteEvent = MutableSharedFlow<ResultState<Boolean>>()
    var deleteEvent = _deleteEvent.asSharedFlow()

    fun getCarts(userID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase.getCart(userID)) {
                is ResultState.Success -> _viewState.update { ResultState.Success(result.data) }
                is ResultState.Error -> _viewState.update { ResultState.Error(result.message) }
                else -> Unit
            }
        }
    }

    fun deleteCart(cartID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase.deleteCart(cartID)) {
                is ResultState.Success -> {
                    _deleteEvent.emit(ResultState.Success(result.data))
                }
                is ResultState.Error -> _deleteEvent.emit(ResultState.Error(result.message))
                else -> _deleteEvent.emit(ResultState.Loading)
            }
        }
    }
}