package com.bruno.applistadecomprascompose.model

data class ItemData(
    val title: String,
    var checked: Boolean,
    var id: Int? = null
)