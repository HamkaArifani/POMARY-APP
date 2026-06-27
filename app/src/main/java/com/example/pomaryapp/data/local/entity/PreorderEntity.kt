package com.example.pomaryapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preorders")
data class PreorderEntity (
    @PrimaryKey val preorderId: String,
    val title: String,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "total_cost") val totalCost: Long,
    @ColumnInfo(name = "selling_price") val sellingPrice: Long,
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "end_date") val endDate: Long,
    @ColumnInfo(name = "total_orders") val totalOrders: Int = 0,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false
)