package com.bruno.applistadecomprascompose.ui

import android.graphics.Bitmap
import android.media.Image
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import com.bruno.applistadecomprascompose.repositories.GroceryRepository
import com.bruno.applistadecomprascompose.model.ItemData
import com.bruno.applistadecomprascompose.model.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
   Precisamos dizer que ele é um "HiltViewModel" e adicionar
   a injeção de dependência no Construtor do "GroceryRepository"
*/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GroceryRepository
): ViewModel() {

//    val state: State<RequestStatus> = repository.state

    /* Atualizando para aplicar o UDF para a fonte de dados local, isso faz com que os dados
       fluam em tempo real */
    val allItems: Flow<List<GroceryItemEntity>> = repository.getItems() // FLow retorna sequência de dados sempre que os itens são alterados, assim o ViewModel avisa a View para renderizar

    /* usamos o "init" para adquirir pela primeira vez os itens da lista*/
   /*
   // não precisamos desse bloco para retorna dados do banco local
   init {
        viewModelScope.launch { // usado para lançar os métodos para a execução
            repository.getItems()
        }
    }*/

    fun addItem( title: String, image: Bitmap, date: String ) {
        viewModelScope.launch {
            repository.addItem( title, image, date )
        }
    }

//    fun removeItem(item: ItemData) {
    fun removeItem(item: GroceryItemEntity) {
        viewModelScope.launch {
            repository.removeItem( item )
        }
    }

//    fun updateItem(item: ItemData) {
    fun updateItem( itemId: Long ) {
        viewModelScope.launch {
            repository.updateItem( itemId )
        }
    }
}