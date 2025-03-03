package com.bruno.applistadecomprascompose

import android.content.Context
import androidx.room.Room
import com.bruno.applistadecomprascompose.database.GroceryDao
import com.bruno.applistadecomprascompose.database.GroceryDatabase
import com.bruno.applistadecomprascompose.database.GroceryLocalDataSource
import com.bruno.applistadecomprascompose.database.MIGRATION_1_2
import com.bruno.applistadecomprascompose.network.GroceryDataSource
import com.bruno.applistadecomprascompose.network.GroceryNetworkService
import com.bruno.applistadecomprascompose.repositories.GroceryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/* Nosso app dependência de injeção diz ao Hilt como ele deve construir
   a nossa árvore de dependência

    O "SingletonComponent"  significa que tudo aqui
    terá uma única instância para ser acessível ao aplicativo
*/


@Module
@InstallIn(SingletonComponent::class)
class AppDI {

    @Provides
    @Singleton
    fun provideGroceryNetWorkService() = GroceryNetworkService() // criando uma instância de "GroceryNetworkService"

    @Provides
    @Singleton
    fun provideGroceryDataSource( groceryNetworkService: GroceryNetworkService) =
        GroceryDataSource(groceryNetworkService)


//    @Provides
//    @Singleton
//    fun provideGroceryRepository( groceryDataSource: GroceryDataSource) =
//        GroceryRepository(groceryDataSource)

    @Provides
    @Singleton
    fun provideGroceryRepository( groceryLocalDataSource: GroceryLocalDataSource) =
        GroceryRepository(groceryLocalDataSource)



/*     É preciso prover a instância do GroceryDao também, usando nossa injeção de dependência do DaggerHilt.
       Lembrando que ele é retornado por nossa classe abstrata "GroceryDatabase" que não pode gerar instâncias,
       mas o faz por causa do "Room", lembre-se que "GroceryDatabase" é que faz a implementação
       da interface que é o "GroceryDao"
*/
    @Provides
    @Singleton
    fun provideGroceryDao(groceryDatabase: GroceryDatabase): GroceryDao {
        return groceryDatabase.groceryDao()
    }

    @Provides
    @Singleton
    fun provideGroceryDatabase(
        @ApplicationContext applicationContext: Context // anotação do DaggerHilt para adquirir o contexto da aplicação, via parâmetro, sem precisar criar ele aqui dentro
    ): GroceryDatabase{ // como "GroceryDatabase" é uma classe abstrata, não podemos fazer uma instância dela, usamos o Room pra criar uma instância desse banco de dados

        /* A biblioteca Room tem a classe do Banco de dados que abstrai pra gente toda parte de
           criar e configurar o SQLite, só precisamos usá-lo.

            Ela têm as DAOs que são objetos que fornecem métodos de acesso ao banco de daodos para
            atualizar, inserir, consultar, etc.
            Ela têm também as "Entities", que representam as tabelas no banco de dados do app

         */
        return Room.databaseBuilder(
                applicationContext,
                GroceryDatabase::class.java, // passando a classe abstrata que representa o GroceryDatabase
                "grocery_database"
            ).addMigrations(MIGRATION_1_2).build() // assim o Room poderá construir nosso banco de dados, e já coloca a versão certa do banco de dados com a migração

    }
}