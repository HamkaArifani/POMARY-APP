package com.example.pomaryapp.ui.preorder.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.usecase.order.GetOrdersUseCase
import com.example.pomaryapp.domain.usecase.preorder.DeletePreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.FinishPreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.GetPreorderDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreorderDetailViewModel @Inject constructor(
    private val deleteUseCase: DeletePreorderUseCase,
    private val finishUseCase: FinishPreorderUseCase,
    private val getDetailUseCase: GetPreorderDetailUseCase,
    private val getOrdersUseCase: GetOrdersUseCase
): ViewModel(){
    private val _preorder = MutableStateFlow<PreorderModel?>(null)
    val preorder = _preorder.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderModel?>>(emptyList())
    val orders = _orders.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _preorder.value = getDetailUseCase(id)
            getOrdersUseCase(id).collect { _orders.value = it }
        }
    }

    fun finishPO() {
        viewModelScope.launch {
            _preorder.value?.let {
                finishUseCase(it)
                load(it.preorderId)
            }
        }
    }

    fun deletePO(onDone: () -> Unit) {
        viewModelScope.launch {
            _preorder.value?.let { deleteUseCase(it) }
            onDone()
        }
    }
}