package com.bruno.applistadecomprascompose.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import kotlinx.coroutines.flow.Flow

/* Classe que cria a interface para acessar e fazer operações
* no banco de dados. Utilize a notação @Dao */

@Dao
interface GroceryDao {

    @Query("SELECT * FROM grocery_items")
    fun getItems(): Flow<List<GroceryItemEntity>> // não  precisamos do "suspend" aqui, pois usamos o "Flow" para fazer a sequência

    @Insert(onConflict = OnConflictStrategy.REPLACE) // se tiver conflito, ou seja, se o item já existe, apenas atualiza-o
    suspend fun addItem(item: GroceryItemEntity) // precisamos do "suspend" aqui, pois não usamos o "Flow" para fazer a sequência

    @Delete
    suspend fun removeItem( item: GroceryItemEntity)

    @Query(" UPDATE grocery_items SET checked = CASE WHEN checked = 0 " +
                                                    " THEN 1 ELSE 0 " +
                                                    " END " +
            " WHERE id = :itemId ") // vamos inverter o valor do campo "checked" no Banco de dados usando a cláusula CASE WHEN
    suspend fun updateItem( itemId: Long )


}

