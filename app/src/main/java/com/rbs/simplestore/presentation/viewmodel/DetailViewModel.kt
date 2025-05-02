package com.rbs.simplestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCase: ProductUseCase
) : ViewModel() {
    private var _viewState = MutableSharedFlow<ResultState<Boolean>>()
    val viewState = _viewState.asSharedFlow()

    fun addCart(request: CartDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.emit(ResultState.Loading)
            when (val result = useCase.addCart(request)) {
                is ResultState.Success -> _viewState.emit(ResultState.Success(result.data))
                is ResultState.Error -> _viewState.emit(ResultState.Error(result.message))
                else -> Unit
            }
        }
    }
}