package com.example.implab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.implab.data.models.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductsDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    companion object {
        @Volatile
        private var INSTANCE: ProductsDataBase? = null

        fun getDatabase(context: Context): ProductsDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductsDataBase::class.java,
                    "products_table"
                )
                    .fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}