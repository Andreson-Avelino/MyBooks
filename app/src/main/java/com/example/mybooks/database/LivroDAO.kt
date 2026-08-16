package com.example.mybooks.database

import android.content.ContentValues
import android.content.Context
import com.example.mybooks.model.Livro
class LivroDAO(context: Context) {
    private val databaseHelper = DatabaseHelper(context)

    // Insere um novo livro no banco de dados e retorna o ID criado pelo SQLite.
    fun inserirLivro(livro: Livro): Long {
        val db = databaseHelper.writableDatabase
        val valores = ContentValues().apply {
            put("titulo", livro.titulo)
            put("autor", livro.autor)
            put("sinopse", livro.sinopse)
            put("favorito", if (livro.favorito) 1 else 0)
        }
        val id = db.insert(
            "livros",
            null,
            valores
        )
        db.close()
        return id
    }

    // Busca todos os livros cadastrados no banco e devolve uma lista de objetos Livro.
    fun listarTodos(): List<Livro> {

        val listaLivros = mutableListOf<Livro>()
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM livros ORDER BY titulo ASC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                val livro = Livro(
                    id = it.getInt(
                        it.getColumnIndexOrThrow("id")
                    ),
                    titulo = it.getString(
                        it.getColumnIndexOrThrow("titulo")
                    ),
                    autor = it.getString(
                        it.getColumnIndexOrThrow("autor")
                    ),
                    sinopse = it.getString(
                        it.getColumnIndexOrThrow("sinopse")
                    ),
                    favorito = it.getInt(
                        it.getColumnIndexOrThrow("favorito")
                    ) == 1
                )
                listaLivros.add(livro)
            }
        }
        db.close()
        return listaLivros
    }

    // Busca um livro específico no banco usando o seu ID.
    fun buscarPorId(id: Int): Livro? {
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM livros WHERE id = ?",
            arrayOf(id.toString())
        )

        var livro: Livro? = null
        cursor.use {

            if (it.moveToFirst()) {
                livro = Livro(
                    id = it.getInt(
                        it.getColumnIndexOrThrow("id")
                    ),
                    titulo = it.getString(
                        it.getColumnIndexOrThrow("titulo")
                    ),
                    autor = it.getString(
                        it.getColumnIndexOrThrow("autor")
                    ),
                    sinopse = it.getString(
                        it.getColumnIndexOrThrow("sinopse")
                    ),
                    favorito = it.getInt(
                        it.getColumnIndexOrThrow("favorito")
                    ) == 1
                )
            }
        }
        db.close()
        return livro
    }

    // Atualiza no banco se o livro está marcado como favorito ou não.
    fun atualizarFavorito(id: Int, favorito: Boolean): Int {
        val db = databaseHelper.writableDatabase

        val valores = ContentValues().apply {
            put("favorito", if (favorito) 1 else 0)
        }
        val linhasAtualizadas = db.update(
            "livros",
            valores,
            "id = ?",
            arrayOf(id.toString())
        )
        db.close()
        return linhasAtualizadas
    }
}