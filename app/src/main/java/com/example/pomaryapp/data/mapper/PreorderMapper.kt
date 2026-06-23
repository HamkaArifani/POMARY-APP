package com.example.pomaryapp.data.mapper

import com.example.pomaryapp.data.local.entity.PreorderEntity
import com.example.pomaryapp.data.remote.dto.PreorderDto
import com.example.pomaryapp.domain.model.PreorderModel

fun PreorderEntity.toDomain(): PreorderModel{
    return PreorderModel(
        preorderId = preorderId,
        title = title,
        totalCost = totalCost,
        sellingPrice = sellingPrice,
        startDate = startDate,
        endDate = endDate,
        totalOrders = totalOrders,
        isCompleted = isCompleted
    )
}

fun PreorderModel.toEntity(): PreorderEntity{
    return PreorderEntity(
        preorderId = preorderId,
        title = title,
        totalCost = totalCost,
        sellingPrice = sellingPrice,
        startDate = startDate,
        endDate = endDate,
        totalOrders = totalOrders,
        isCompleted = isCompleted
    )
}

fun PreorderDto.toEntity(): PreorderEntity{
    return PreorderEntity(
        preorderId = preorderId,
        title = title,
        totalCost = totalCost,
        sellingPrice = sellingPrice,
        startDate = startDate,
        endDate = endDate,
        totalOrders = totalOrders,
        isCompleted = isCompleted
    )
}

fun PreorderEntity.toDto(): PreorderDto{
    return PreorderDto(
        preorderId = preorderId,
        title = title,
        totalCost = totalCost,
        sellingPrice = sellingPrice,
        startDate = startDate,
        endDate = endDate,
        totalOrders = totalOrders,
        isCompleted = isCompleted
    )
}