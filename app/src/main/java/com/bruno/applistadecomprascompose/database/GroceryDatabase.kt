package com.bruno.applistadecomprascompose.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bruno.applistadecomprascompose.model.GroceryItemEntity
import com.bruno.applistadecomprascompose.utils.Converters

/* Criando o DataBase e passa para ele nossa lista de
    entidades/tabelas e a versão do banco.
    Ele precisa ser abstrato e estender de de RoomDataBase.
*/

// criando a variável de migração do banco para atualizar de versão o banco local do app de versão antiga para mais nova

/*
//val MIGRATION_2_3 = object : Migration(2,3) { // "object" é um objeto anônimo
val MIGRATION_1_2 = object : Migration(1,2) { // "object" é um objeto anônimo
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE grocery_items ADD COLUMN date TEXT DEFAULT '-'") // QUERY sql que adiciona uma coluna a tabela no banco, alterando sua estrutura
    }
}
*/

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Cria uma nova tabela com a estrutura correta
        database.execSQL("CREATE TABLE IF NOT EXISTS `grocery_items_new` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `checked` INTEGER NOT NULL, `date` TEXT, `image` BLOB, PRIMARY KEY(`id`))")

        // Copia os dados da tabela antiga para a nova
//        database.execSQL("INSERT INTO `grocery_items_new` (`id`, `title`, `checked`, `date`, `image`) SELECT `id`, `title`, `checked`, `date`, `image` FROM `grocery_items`")
        database.execSQL("INSERT INTO `grocery_items_new` (`id`, `title`, `checked`) SELECT `id`, `title`, `checked` FROM `grocery_items`")

        // Remove a tabela antiga
        database.execSQL("DROP TABLE `grocery_items`")

        // Renomeia a nova tabela para o nome original
        database.execSQL("ALTER TABLE `grocery_items_new` RENAME TO `grocery_items`")
    }
}

@TypeConverters(Converters::class)
@Database(entities = [GroceryItemEntity::class], version = 2) // atualizamos a versão pois adicionamos nova coluna na "GroceryItemEntity"
abstract class GroceryDatabase: RoomDatabase() {
    abstract fun groceryDao(): GroceryDao
}