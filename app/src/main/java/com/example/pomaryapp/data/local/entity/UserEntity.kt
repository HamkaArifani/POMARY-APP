package com.example.pomaryapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity (
    @PrimaryKey (autoGenerate = true) val userId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "pin") val pin: String,
    @ColumnInfo(name = "has_completed_setup") val hasCompletedSetup: Boolean = false
)