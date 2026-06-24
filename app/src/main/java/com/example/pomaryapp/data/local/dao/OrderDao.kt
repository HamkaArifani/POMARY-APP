package com.example.pomaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomaryapp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE preorderId = :preorderId")
    fun getOrdersByPreorder(preorderId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE preorderId = :preorderId")
    suspend fun getOrdersByPreorderSync(preorderId: String): List<OrderEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE orderId = :id")
    suspend fun getOrderById(id: String): OrderEntity?
}