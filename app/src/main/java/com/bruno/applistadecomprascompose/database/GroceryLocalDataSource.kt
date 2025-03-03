package com.bruno.applistadecomprascompose.database

import android.graphics.Bitmap
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import javax.inject.Inject

class GroceryLocalDataSource @Inject constructor(
    private val groceryDao: GroceryDao
){


//    suspend fun getItems() = groceryDao.getItems()
    fun getItems() = groceryDao.getItems()

    suspend fun addItem( title: String, image: Bitmap, date: String ) =
        groceryDao.addItem( GroceryItemEntity( title,false, image = image, date = date ) )

    suspend fun removeItem( item: GroceryItemEntity ) = groceryDao.removeItem( item )

    suspend fun updateItem( itemId: Long ) = groceryDao.updateItem( itemId )


}