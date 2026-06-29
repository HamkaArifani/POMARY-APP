package com.example.pomaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pomaryapp.data.local.entity.PreorderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreorderDao {
    @Query("SELECT * FROM preorders ORDER BY end_date DESC")
    fun getAllPreorders(): Flow<List<PreorderEntity>>

    @Query("SELECT * FROM preorders WHERE preorderId= :preorderId")
    suspend fun getPreorderById(preorderId: String): PreorderEntity?

    @Query("SELECT * FROM preorders")
    suspend fun getAllPreordersSync(): List<PreorderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreorder(preorder: PreorderEntity)

    @Update
    suspend fun updatePreorder(preorder: PreorderEntity)

    @Delete
    suspend fun deletePreorder(preorder: PreorderEntity)

    @Query("DELETE FROM preorders WHERE preorderId = :id")
    suspend fun deletePreorderById(id: String)
}