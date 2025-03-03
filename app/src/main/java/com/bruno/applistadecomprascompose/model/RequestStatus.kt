package com.bruno.applistadecomprascompose.model

/* Simula como se estivéssemos adiquirindo dados remotos
  Para isso passamos os dados e a variável "isLoading"
  para verificar se os dados estão carregando ou não
 */

data class RequestStatus (
    val isLoading: Boolean,
    val data: MutableList<ItemData>
)