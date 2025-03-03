package com.bruno.applistadecomprascompose.network

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.bruno.applistadecomprascompose.model.ItemData
import com.bruno.applistadecomprascompose.model.RequestStatus
import kotlinx.coroutines.delay

/*  Simula uma atividade de adquirir os itens da lista
    a partir de um servidor. COmo se fosse um banco de dados
    remoto.
*/
class GroceryNetworkService {

    private  val itemList: MutableList<ItemData> = mutableListOf() // lista mutável que simula o nosso banco de dados remoto


    /* "state" é público mas não é mutable, pois a partir daqui ninguém poderá alterar
       o nosso state, apenas a classe "GroceryNetworkService" pode alterá-lo.
       As classes e objetos criados acima dele não podem alterá-lo
    */
    private val _status = mutableStateOf(RequestStatus( false, itemList ))

    val state: State<RequestStatus> = _status

    // métodos "suspend" são coroutines, isso indica para o kotlin que são métodos assíncronos
    suspend fun getItems(){
        _status.value = _status.value.copy( isLoading = true) // alterando o "isLoading" para true
        delay(500) // simula um delay de meio segundo, que é como se fosse uma busca na internet
        itemList.addAll(
            mutableListOf(
                ItemData("Maça", false, 0),
                ItemData("Banana", false, 1),
                ItemData("Leite", false, 2),
                ItemData("Laranja", false, 3),
                ItemData("Pão", false, 4)
            )
        )
        _status.value = _status.value.copy( isLoading = false, data = itemList) // alterando o "isLoading" para false e passar os dados da lista atualizada, pré-populada
    }

    suspend fun addItem( title: String ){
        _status.value = _status.value.copy( isLoading = true )
        delay(500)
        println("O title é: $title")
        itemList.add( ItemData(title, false, itemList.size ) )
        _status.value = _status.value.copy( isLoading = false, data = itemList)
    }

    suspend fun removeItem( item: ItemData ){
        _status.value = _status.value.copy( isLoading = true )
        delay(500)
        itemList.remove(item)
        _status.value = _status.value.copy( isLoading = false, data = itemList)
    }

    suspend fun updateItem( item: ItemData ){
        _status.value = _status.value.copy( isLoading = true )
        delay(500)

        val index = itemList.indexOf(item)
        if ( index != -1 ){ // testa se o item existe
            val updatedItens = itemList.toMutableList() // cria uma cópia da lista, senão não conseguiremos modificá-la
            updatedItens[index].checked = !updatedItens[index].checked
            itemList.clear() // limpa o array original
            itemList.addAll(updatedItens) // devolve todos os elementos para o array original
            _status.value = _status.value.copy( isLoading = false, data = itemList)
        }
    }

}


