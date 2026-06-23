package com.example.pomaryapp.data.remote.dto

data class OrderDto (
    val orderId: String = "",
    val preorderId: String = "",
    val buyerName: String = "",
    val buyerPhone: String = "",
    val itemPrice: Long = 0L,
    val quantity: Int = 0,
    val note: String? = null,
    val createdAt: Long = 0L
)