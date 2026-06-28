package com.example.pomaryapp.domain.usecase.order

import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import javax.inject.Inject

class UpsertOrderUseCase @Inject constructor(
    private val repository: PreorderRepository
) {
    suspend operator fun invoke(order: OrderModel, isEdit: Boolean) {
        if (isEdit) {
            repository.updateOrder(order)
        } else {
            repository.insertOrder(order)
        }
    }
}