package com.bruno.applistadecomprascompose.repositories

import android.graphics.Bitmap
import androidx.compose.runtime.State
import com.bruno.applistadecomprascompose.database.GroceryLocalDataSource
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import com.bruno.applistadecomprascompose.network.GroceryDataSource
import com.bruno.applistadecomprascompose.model.ItemData
import com.bruno.applistadecomprascompose.model.RequestStatus
import javax.inject.Inject

/*
    A "GroceryRepository" consome de 2 fontes de dados, a remota do pacote "networK"
     e a local do pacote "database", mas sem saber de onde vem os dados,
     isso é abstraido para ele.
     Esse padrão de repository pattern facilita trocar o data source
     para outra fonte de dados

*/

class GroceryRepository @Inject constructor(
//    private val dataSource: GroceryDataSource
    private val dataSource: GroceryLocalDataSource
){
     /*adquirindo o nosso status*/
//    val state: State<RequestStatus> = dataSource.state

//    suspend fun getItems() = dataSource.getItems()
    fun getItems() = dataSource.getItems()

    suspend fun addItem( title: String, image: Bitmap, date: String) =
        dataSource.addItem( title, image, date )

    suspend fun removeItem( item: GroceryItemEntity ) = dataSource.removeItem( item )

    suspend fun updateItem( itemId: Long ) = dataSource.updateItem( itemId )

}