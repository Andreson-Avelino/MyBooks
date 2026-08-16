package com.example.mybooks.database

// Context representa o contexto da aplicação Android.
// Ele é necessário para que o SQLite saiba onde criar e acessar o banco.
import android.content.Context
// Representa uma conexão com um banco de dados SQLite.
import android.database.sqlite.SQLiteDatabase
// Classe do Android utilizada para criar e gerenciar bancos SQLite.
import android.database.sqlite.SQLiteOpenHelper
// DatabaseHelper será responsável por criar e atualizar
// o banco de dados local do aplicativo MyBooks.
class DatabaseHelper(context: Context) :

// Chamamos o construtor da classe SQLiteOpenHelper.
// Informamos:
// 1. O contexto da aplicação
// 2. O nome do banco
// 3. CursorFactory (não vamos utilizar, por isso null)
// 4. A versão do banco
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
    // Este método é executado automaticamente
    // na PRIMEIRA vez que o banco de dados é criado.
    override fun onCreate(db: SQLiteDatabase) {
        // Criamos o comando SQL responsável por criar
        // a tabela "livros".

        // Comando SQL responsável por criar a tabela de livros.
        val sql = """
    CREATE TABLE livros (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        titulo TEXT NOT NULL,
        autor TEXT NOT NULL,
        sinopse TEXT NOT NULL,
        favorito INTEGER NOT NULL DEFAULT 0
    )
""".trimIndent()
        // Executa o comando SQL acima no banco.
        db.execSQL(sql)
    }
    // Este método será chamado quando aumentarmos
    // a versão do banco de dados.
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Apaga a tabela antiga caso ela exista.
        // Essa estratégia é simples e suficiente
        // neste estágio inicial do projeto.
        db.execSQL("DROP TABLE IF EXISTS livros")

        // Cria novamente a tabela usando a nova estrutura.
        onCreate(db)
    }
    // companion object permite criar constantes
    // que pertencem à classe DatabaseHelper.
    companion object {
        // Nome do arquivo do banco de dados.
        private const val DATABASE_NAME = "mybooks.db"
        // Versão atual do banco.
        // Caso a estrutura mude no futuro,
        // poderemos aumentar este número.
        private const val DATABASE_VERSION = 1
    }
}