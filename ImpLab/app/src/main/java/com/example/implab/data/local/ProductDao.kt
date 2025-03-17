package com.example.implab.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product) : Long

    @Delete
    suspend fun deleteProduct(product: Product) :Int

    @Query("SELECT * FROM products_table")
    fun getAllProducts(): Flow<List<Product>> //not suspend because it returns flow ,m4 7aga wa7da ,,magmo3a mn el data

}