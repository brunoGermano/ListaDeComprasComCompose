package com.bruno.applistadecomprascompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno.applistadecomprascompose.ui.theme.AppListaDeComprasComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppListaDeComprasComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    GroceryListScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(){
    val  items = remember{
//        mutableListOf( // cria lista que terá estado vinculado ao compose linha criada com mutable errado e não alterava a UI do app porque não era um state observável
        mutableStateListOf( // cria lista que terá estado vinculado ao compose, este tipo, "mutableStateListOf", é o que é obervado
            ItemData("Maça", false, 0),
            ItemData("Banana", false, 1),
            ItemData("Leite", false, 2),
            ItemData("Laranja", false, 3),
            ItemData("Pão", false, 4)
        )
    }

    var newItem by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ){
        TopAppBar(
            title = { Text(text = "Lista de Compras") },
            Modifier.background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Criando o componente de entrada de dados para a lista de compras
        InputSection(
            newItem,
            onNewItemChange = {
                newItem = it
                println("MainActivity::newItem: $it")
            },
            onAddItemClick = {
                println("MainActivity::addItem: $newItem")
                if (newItem.isNotBlank()){
                    items.add(ItemData( newItem,false, items.size ))
                    newItem = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Criando a seção de listagem da lista
        GroceryListSection(
            items,
            onItemCheckedChanged = {
                // cria lógica para atualizar o checkbox
                val index = items.indexOf(it)
                if (index != -1 ) { // se o index for diferente de -1 significa que existe um elemento no array e vamos interagir com ele
                    val updatedItens = items.toMutableList() // cria uma cópia da lista, senão não conseguiremos modificá-la
                    updatedItens[index].checked = !updatedItens[index].checked
                    items.clear() // limpa o array original
                    items.addAll(updatedItens) // devolve todos os elementos para o array original
                }
            },
            onItemClick = {
                items.remove(it)
            }
            )
    }
}


@Composable
fun GroceryListSection(
    list: List<ItemData>,
    onItemCheckedChanged: (ItemData) -> Unit,
    onItemClick: (ItemData) -> Unit
){
    // criando o LazyColumn para mostrar a lista e manusear cada elemento facilmente
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ){
        items(list.size){ index ->
            GroceryItemCard(list[index], onItemCheckedChanged, onItemClick)
        }
    }
}

// composable que mostra cada item da lista baseado no index da lista
@Composable
fun GroceryItemCard(
    item: ItemData,
    onItemCheckedChanged: (ItemData) -> Unit,
    onItemClick: (ItemData) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8F
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.checked,
                onCheckedChange = { onItemCheckedChanged(item) },
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None
            )
            IconButton(onClick = { onItemClick(item) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun InputSection(
    newValue:String,
    onNewItemChange: (String) -> Unit,
    onAddItemClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        // componente personalizado para receber o texto
        PlaceHolderTextField(
            value = newValue,
            onValueChange = { onNewItemChange(it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = "Add a new Item!",
            modifier = Modifier
                .weight(1f)
                .padding(16.dp) // 1 no weight faz ocupar o restante do espaço disponível
        )
            Button(onClick = onAddItemClick ) {
                Text(text = "Add")
            }
    }

}

@Composable
fun PlaceHolderTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit, // função Lambda que recebe uma String de parâmetro e não retorna nada
    textStyle: TextStyle,
    placeholder: String
){
    var isFocused by remember{ mutableStateOf(false) }

    Box(
       modifier = modifier
           .background(Color.White, RoundedCornerShape(8.dp))
           .border(
               width = 2.dp,
               color = if (isFocused) MaterialTheme.colorScheme.primary else Color.LightGray,
               shape = RoundedCornerShape(8.dp)
           )
    ){
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusRequester(FocusRequester()) // fazendo o componente ter a habilidade de detectar se ele está focado ou não
        )
        // Mostra dica para o user saber o que deve escrever dentro do campo
        if (value.isEmpty()){
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroceryListScreenPreview() {
    AppListaDeComprasComposeTheme {

    }
}