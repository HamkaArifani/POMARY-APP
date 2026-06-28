package com.example.pomaryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pomaryapp.data.local.dao.OrderDao
import com.example.pomaryapp.data.local.dao.PreorderDao
import com.example.pomaryapp.data.local.entity.OrderEntity
import com.example.pomaryapp.data.local.entity.PreorderEntity
import com.example.pomaryapp.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PreorderEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PomaryDatabase: RoomDatabase() {
    abstract fun preorderDao(): PreorderDao
    abstract fun orderDao(): OrderDao
}