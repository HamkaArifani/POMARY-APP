package com.example.pomaryapp.domain.model

data class OrderModel (
    val orderId: String,
    val preorderId: String,
    val buyerName: String,
    val buyerPhone: String,
    val itemPrice: Long,
    val quantity: Int,
    val note: String?,
    val createdAt: Long = System.currentTimeMillis()
){
    val totalPrice : Long = itemPrice * quantity.toLong()
}