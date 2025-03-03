package com.bruno.applistadecomprascompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import com.bruno.applistadecomprascompose.model.ItemData
import com.bruno.applistadecomprascompose.model.RequestStatus
import com.bruno.applistadecomprascompose.ui.theme.AppListaDeComprasComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

/*
    Tudo relacionado a interface e a VIew está no pacote "UI", a "MainActivity"
    é a view neste projeto.
    Tudo relacionado a data, requisição dos dados e etc, está no pacote "datalayer"
    Tudo relacionado a modelo de dados, as estruturas deles, está no pacote "model"

    Possui dependências do Dagger Hilt e outros.

    Usamos a notação "@AndroidEntryPoint" para que o
    Dagger Hilt se localize na sua árvore de dependências

 */

// https://meet.jit.si/meetWithJean_03_03_25

@AndroidEntryPoint
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
fun GroceryListScreen( viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel() ){ // não precisa injetar o viwModel como parâmetro, é só passar ele direto para o composable
    /* Não usaremos mais este modo para a fonte de dados local*/
    //val items = viewModel.state // adquirindo o state do viewModel, Anny muda o nome de "items" para "data", já que dentro dele teremos o isLoading e a lista, mas eu não irei mudar para não confundir com o código antigo antes do viewModel.

    // Usaremos esse modo de coletar os dados pela UI como um state
    val items = viewModel.allItems.collectAsState(initial = emptyList())


  /*  val  items = remember{
//        mutableListOf( // cria lista que terá estado vinculado ao compose linha criada com mutable errado e não alterava a UI do app porque não era um state observável
        mutableStateListOf( // cria lista que terá estado vinculado ao compose, este tipo, "mutableStateListOf", é o que é obervado
            ItemData("Maça", false, 0),
            ItemData("Banana", false, 1),
            ItemData("Leite", false, 2),
            ItemData("Laranja", false, 3),
            ItemData("Pão", false, 4)
        )
    }*/

//    var newItem by remember { mutableStateOf("") } // comentado para adicionar a imagem no campo
    var newItem by rememberSaveable { mutableStateOf("") } // garante que quando voltar da activity da foto, a foto fique salva nessa activity, assim persiste o valor do "newItem" mesmo que outra activity seja aberta


    // Cria um launcher de uma nova activity para tirar fotos
        val pickMedia = rememberLauncherForActivityResult( ActivityResultContracts.TakePicturePreview() ) {

        //Dentro deste callback do objeto "pickMedia", o usário pode ou não tirar uma foto, por isso usar o "let"

        it?.let { // "it" é a activity da câmera que abre, pode dar erro e não abrir por isso o "?" de nullable
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val date = simpleDateFormat.format(Date())
            viewModel.addItem( newItem, it, date )
            newItem = ""
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally //incrementado para centralizar componentes na tela
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

                    /* Não faremos mais a adição, atulização e remoção de dados da nossa lista
                     diretamente da view, vamos usar o viewModel para falar/atualizar a camada
                     de dados com o evento da view. Nao precisamos mais fazer lógica na view,
                     a view apenas passará o evento para o viewModel e ele irá se comunicar
                     com a camada de dataLayer e fazer as atualizações necessárias */

//                    items.add(ItemData( newItem,false, items.size )) //linha com o modo usado antes da implementação do viewModel comentado
//                    viewModel.addItem( newItem ) // passamos o item que queremos criar, e precisamos tornar o "id" um campo nullable na classe ItemData
//                    newItem = ""

                    /* Acrescentado para adicionar parte da foto. Chama a tela de captura da camera
                       com o "launch", no seu callBack a teremos a imagem da
                       captura da câmera e de fato adicionaremos o item*/
                    pickMedia.launch()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*Comenta o loading pois não estaremos mais simulando busca
          na internet já que os dados são locais agora */

//        LoadingProgressBar(items.value.isLoading)

        //Criando a seção de listagem da lista
        GroceryListSection(
            items,
            onItemCheckedChanged = {
              /* // Lógica antiga comentada, antes de implementar o viewModel

              // cria lógica para atualizar o checkbox
                val index = items.indexOf(it)
                if (index != -1 ) { // se o index for diferente de -1 significa que existe um elemento no array e vamos interagir com ele
                    val updatedItens = items.toMutableList() // cria uma cópia da lista, senão não conseguiremos modificá-la
                    updatedItens[index].checked = !updatedItens[index].checked
                    items.clear() // limpa o array original
                    items.addAll(updatedItens) // devolve todos os elementos para o array original
                }
                
                */
                viewModel.updateItem(it.id)
            },
            onItemClick = {
//                items.remove(it) //linha com o modo usado antes da implementação do viewModel comentado
                viewModel.removeItem(it)
            }
        )
    }
}


@Composable
fun LoadingProgressBar( isLoading: Boolean ){
    if(isLoading){
        CircularProgressIndicator(
            modifier = Modifier.size(49.dp).padding(16.dp)
        )
    }
}

@Composable
fun GroceryListSection(
//    list: List<ItemData>, //linha com o modo usado antes da implementação do viewModel comentado
//    list: State<RequestStatus>, // deve ser atualizado para pegar dados locais
//    onItemCheckedChanged: (ItemData) -> Unit,
//    onItemClick: (ItemData) -> Unit

    list: State<List<GroceryItemEntity>>, // agora ele tem que usar a lista de "GroceryItemEntity" do modelo de dados local
    onItemCheckedChanged: (GroceryItemEntity) -> Unit,
    onItemClick: (GroceryItemEntity) -> Unit
){
    // criando o LazyColumn para mostrar a lista e manusear cada elemento facilmente
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ){
        /* Bloco com o modo usado antes da implementação do viewModel comentado
        items(list.size){ index ->
            GroceryItemCard( list[index], onItemCheckedChanged, onItemClick )
        */
//        items(list.value.data){ item -> // o "items" precisa ser do tipo lista agora
        items(list.value){ item -> // deve ficar assim pro modelo de dados local
            GroceryItemCard( item, onItemCheckedChanged, onItemClick )
        }
    }
}

// composable que mostra cada item da lista baseado no index da lista
@Composable
fun GroceryItemCard(
//    item: ItemData,
//    onItemCheckedChanged: (ItemData) -> Unit,
//    onItemClick: (ItemData) -> Unit
    item: GroceryItemEntity, // deve trocar o tipo para atender ao modelo de dados local
    onItemCheckedChanged: (GroceryItemEntity) -> Unit,
    onItemClick: (GroceryItemEntity) -> Unit
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
        Column {

            // Caso tenha uma imagem, mostramos ela
            item.image?.let {
                Image(
                    bitmap = it.asImageBitmap(), // it se refere a imagem retornada de callback
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop // faz a imagem ocupar toodo o espaço do seu container mesmo que ela fique cortada, mas ela não deforma
                )
            }

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
                    text = "${item.title} ${item.date}",
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
}

@Composable
fun InputSection(
    newValue: String,
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
    GroceryListScreen()
    //AppListaDeComprasComposeTheme {}
}