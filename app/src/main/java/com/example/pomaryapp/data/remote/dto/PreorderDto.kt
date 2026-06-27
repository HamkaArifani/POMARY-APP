package com.example.pomaryapp.data.remote.dto

data class PreorderDto (
    val preorderId: String = "",
    val productName: String = "",
    val title: String = "",
    val totalCost: Long = 0L,
    val sellingPrice: Long = 0L,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val totalOrders: Int = 0,
    val isCompleted: Boolean = false
)