package com.example.pomaryapp.domain.repository

import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.PreorderModel
import kotlinx.coroutines.flow.Flow

interface PreorderRepository {
    fun getActivePreorders(): Flow<List<PreorderModel>>
    suspend fun getActivePreordersSync(): List<PreorderModel>

    fun getCompletedPreorders(): Flow<List<PreorderModel>>
    suspend fun getPreordersById(id: String): PreorderModel?
    suspend fun insertPreorder(preorder: PreorderModel)
    suspend fun updatePreorder(preorder: PreorderModel)
    suspend fun deletePreorder(preorder: PreorderModel)

    fun getOrdersByPreorderId(preorderId: String): Flow<List<OrderModel>>
    fun getOrdersByPreorderIdRealtime(preorderId: String): Flow<List<OrderModel>>
    suspend fun getOrderById(orderId: String): OrderModel?
    suspend fun insertOrder(order: OrderModel)
    suspend fun updateOrder(order: OrderModel)
    suspend fun deleteOrder(order: OrderModel)

    suspend fun syncWithRemote()
}