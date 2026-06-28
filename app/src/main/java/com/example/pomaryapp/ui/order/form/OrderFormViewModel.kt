package com.example.pomaryapp.ui.order.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import com.example.pomaryapp.domain.usecase.order.DeleteOrderUseCase
import com.example.pomaryapp.domain.usecase.order.GetOrderDetailUseCase
import com.example.pomaryapp.domain.usecase.order.UpsertOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrderFormViewModel @Inject constructor(
    private val upsertOrderUseCase: UpsertOrderUseCase,
    private val preorderRepository: PreorderRepository,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase
): ViewModel() {
    var buyerName by mutableStateOf("")
    var buyerPhone by mutableStateOf("")
    var buyerQuantity by mutableStateOf("")
    var note by mutableStateOf("")
    var itemPrice by mutableStateOf(0L)

    fun loadOrderData(orderId: String) {
        viewModelScope.launch {
            getOrderDetailUseCase(orderId)?.let {
                buyerName = it.buyerName
                buyerPhone = it.buyerPhone
                buyerQuantity = it.buyerQuantity.toString()
                note = it.note ?: ""
                itemPrice = it.itemPrice
            }
        }
    }

    fun initPrice(preorderId: String) {
        viewModelScope.launch {
            preorderRepository.getPreordersById(preorderId)?.let {
                itemPrice = it.sellingPrice
            }
        }
    }

    fun saveOrder(preorderId: String, orderId: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val order = OrderModel(
                orderId = orderId ?: UUID.randomUUID().toString(),
                preorderId = preorderId,
                buyerName = buyerName,
                buyerPhone = buyerPhone,
                itemPrice = itemPrice,
                buyerQuantity = buyerQuantity.toIntOrNull() ?: 0,
                note = note
            )
            upsertOrderUseCase(order, isEdit = orderId != null)
            onSuccess()
        }
    }

    fun deleteOrder(orderId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            getOrderDetailUseCase(orderId)?.let {
                deleteOrderUseCase(it)
                onDone()
            }
        }
    }
}