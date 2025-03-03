package com.bruno.applistadecomprascompose.model

import android.graphics.Bitmap
import android.widget.CheckBox
import androidx.room.Entity
import androidx.room.PrimaryKey

/* Possibilita fazer a interação com o banco de dados
* criando uma tabela, que tem o mesmo nome de atributos que
* usamos na classe "ItemData", de modo a não quebrar o código
* de outras classes quando alterarmos a fonte de dados remota para local */

@Entity(tableName = "grocery_items")
data class GroceryItemEntity (
    val title: String,
    val checked: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // define o atributo "id" como chave primária
    val image: Bitmap? = null,
    val date: String? = null
)