package com.example.implab.data.local

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

class ProductsLocalDataSource (private val productDao: ProductDao):LocalDataSource{
    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product): Int {

return if(product!=null){
    productDao.deleteProduct(product)
}else{
    -1
}
}
    override fun getAllProducts(): Flow<List<Product>> {
return productDao.getAllProducts() //law suspend m4 7y3ml error ,, el data already m3aya
    }
}