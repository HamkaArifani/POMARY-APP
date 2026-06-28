package com.example.pomaryapp.domain.usecase.order

import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import javax.inject.Inject

class DeleteOrderUseCase @Inject constructor(private val repository: PreorderRepository) {
    suspend operator fun invoke(order: OrderModel) {
        repository.deleteOrder(order)
    }
}