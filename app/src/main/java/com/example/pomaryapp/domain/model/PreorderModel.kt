package com.example.pomaryapp.domain.model

data class PreorderModel (
    val preorderId: String,
    val title: String,
    val totalCost: Long,
    val sellingPrice: Long,
    val startDate: Long,
    val endDate: Long,
    val totalOrders: Int = 0,
    val isCompleted: Boolean = false
){
    val profitPerItem: Long = sellingPrice - totalCost
    val totalGrossProfit: Long = sellingPrice * totalOrders
    val totalCleanProfit: Long = profitPerItem * totalOrders
}