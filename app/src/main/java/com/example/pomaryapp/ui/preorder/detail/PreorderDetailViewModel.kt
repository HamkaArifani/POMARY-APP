package com.example.pomaryapp.ui.preorder.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.model.UserModel
import com.example.pomaryapp.domain.usecase.auth.GetSessionDataUseCase
import com.example.pomaryapp.domain.usecase.order.GetOrdersRealTimeUseCase
import com.example.pomaryapp.domain.usecase.order.GetOrdersUseCase
import com.example.pomaryapp.domain.usecase.preorder.DeletePreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.FinishPreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.GetPreorderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreorderDetailViewModel @Inject constructor(
    private val deleteUseCase: DeletePreorderUseCase,
    private val finishUseCase: FinishPreorderUseCase,
    private val getDetailUseCase: GetPreorderDetailUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getOrdersRealTimeUseCase: GetOrdersRealTimeUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
): ViewModel(){
    private val _preorder = MutableStateFlow<PreorderModel?>(null)
    val preorder: StateFlow<PreorderModel?> = _preorder.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderModel>>(emptyList())
    val orders: StateFlow<List<OrderModel>> = _orders.asStateFlow()

    val showRecapDialog = mutableStateOf(false)

    val userSession: StateFlow<UserModel?> = getSessionDataUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun load(id: String) {
        viewModelScope.launch {
            _preorder.value = getDetailUseCase(id)
            getOrdersRealTimeUseCase(id).collect { _orders.value = it }
        }
    }

    fun finishPo() {
        viewModelScope.launch {
            _preorder.value?.let { currentPo ->
                finishUseCase(currentPo)
                _preorder.value = getDetailUseCase(currentPo.preorderId)
            }
        }
    }

    fun deletePo(onDone: () -> Unit) {
        viewModelScope.launch {
            _preorder.value?.let {
                deleteUseCase(it)
                onDone()
            }
        }
    }
}