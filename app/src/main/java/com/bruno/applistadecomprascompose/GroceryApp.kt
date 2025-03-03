package com.bruno.applistadecomprascompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/* Aqui dizemos que o nosso Application é um aplicativo que usará o Hilt,
*  ou seja, será um android app.
*  Isso diz para o app que ele deve usar o Hilt para construir
*  raiz de árvore de dependência.
*  Deve-se ir no "manifest" e mudar o "name" para "GroceryApp" também.
*  */

@HiltAndroidApp
class GroceryApp : Application()