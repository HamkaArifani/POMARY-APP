package com.example.pomaryapp.domain.usecase.order

import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(private val preorderRepository: PreorderRepository) {
    operator fun invoke(preorderId: String): Flow<List<OrderModel>>{
        return preorderRepository.getOrdersByPreorderId(preorderId)
    }
}