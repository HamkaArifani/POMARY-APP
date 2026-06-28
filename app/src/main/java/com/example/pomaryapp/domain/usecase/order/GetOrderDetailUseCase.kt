package com.example.pomaryapp.domain.usecase.order

import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import javax.inject.Inject

class GetOrderDetailUseCase @Inject constructor(
    private val repository: PreorderRepository
){
    suspend operator fun invoke(orderId: String): OrderModel? {
        return repository.getOrderById(orderId)
    }
}