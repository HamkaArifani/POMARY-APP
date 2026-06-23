package com.example.pomaryapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = PreorderEntity::class,
            parentColumns = ["preorderId"],
            childColumns = ["preorderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderEntity (
    @PrimaryKey val orderId: String,
    val preorderId: String,
    @ColumnInfo(name = "buyer_name") val buyerName: String,
    @ColumnInfo(name = "buyer_phone") val buyerPhone: String,
    @ColumnInfo(name = "item_price") val itemPrice : Long,
    val quantity: Int,
    val note: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)