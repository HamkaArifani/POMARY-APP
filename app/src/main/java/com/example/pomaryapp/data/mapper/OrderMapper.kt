package com.example.pomaryapp.data.mapper

import com.example.pomaryapp.data.local.entity.OrderEntity
import com.example.pomaryapp.data.local.entity.UserEntity
import com.example.pomaryapp.data.remote.dto.OrderDto
import com.example.pomaryapp.data.remote.dto.UserDto
import com.example.pomaryapp.domain.model.OrderModel
import com.example.pomaryapp.domain.model.UserModel

fun OrderEntity.toDomain(): OrderModel {
    return OrderModel(
        orderId = orderId,
        preorderId = preorderId,
        buyerName = buyerName,
        buyerPhone =  buyerPhone,
        itemPrice = itemPrice,
        buyerQuantity = buyerQuantity,
        note = note,
        createdAt = createdAt
    )
}

fun OrderModel.toEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        preorderId = preorderId,
        buyerName = buyerName,
        buyerPhone = buyerPhone,
        itemPrice = itemPrice,
        buyerQuantity = buyerQuantity,
        note = note,
        createdAt = createdAt
    )
}

fun OrderDto.toEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        preorderId = preorderId,
        buyerName = buyerName,
        buyerPhone = buyerPhone,
        itemPrice = itemPrice,
        buyerQuantity = buyerQuantity,
        note = note,
        createdAt = createdAt
    )
}

fun OrderEntity.toDto(): OrderDto{
    return OrderDto(
        orderId = orderId,
        preorderId = preorderId,
        buyerName = buyerName,
        buyerPhone = buyerPhone,
        itemPrice = itemPrice,
        buyerQuantity = buyerQuantity,
        note = note,
        createdAt = createdAt
    )
}

fun OrderModel.toDto(): OrderDto{
    return OrderDto(
        orderId = orderId,
        preorderId = preorderId,
        buyerName = buyerName,
        buyerPhone = buyerPhone,
        itemPrice = itemPrice,
        buyerQuantity = buyerQuantity,
        note = note,
        createdAt = createdAt
    )
}