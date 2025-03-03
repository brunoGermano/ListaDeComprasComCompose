package com.bruno.applistadecomprascompose.network

import androidx.compose.runtime.State
import com.bruno.applistadecomprascompose.model.ItemData
import com.bruno.applistadecomprascompose.model.RequestStatus
import javax.inject.Inject

/*
    "GroceryDataSource" irá consumir o nosso "GroceryNetworkService" fake
    Usamos o "inject" do Dagger Hilt para injetar a dependência

*/

class GroceryDataSource @Inject constructor(
    private val networkService: GroceryNetworkService
){
    // adquirindo o nosso status
    val state: State<RequestStatus> = networkService.state

    suspend fun getItems() = networkService.getItems()

    suspend fun addItem( title: String) = networkService.addItem( title )

    suspend fun removeItem(item: ItemData ) = networkService.removeItem(item)

    suspend fun updateItem(item: ItemData ) = networkService.updateItem(item)

}