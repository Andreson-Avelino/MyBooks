package com.example.mybooks

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybooks.adapter.LivroAdapter
import com.example.mybooks.database.LivroDAO
class MainActivity : AppCompatActivity() {

    // Mantém o acesso ao banco e o adaptador da lista de livros.
    private lateinit var livroDAO: LivroDAO
    private lateinit var livroAdapter: LivroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configura o banco, o RecyclerView e o Adapter da tela principal.
        livroDAO = LivroDAO(this)
        val recyclerLivros =
            findViewById<RecyclerView>(R.id.recyclerLivros)

        livroAdapter = LivroAdapter(emptyList()) { livro ->
            val intent = Intent(
                this,
                DetalhesLivroActivity::class.java
            )
            intent.putExtra("livro_id", livro.id)
            startActivity(intent)
        }
        recyclerLivros.layoutManager =
            LinearLayoutManager(this)
        recyclerLivros.adapter =
            livroAdapter

        val buttonCadastrarLivro =
            findViewById<Button>(R.id.buttonCadastrarLivro)
        buttonCadastrarLivro.setOnClickListener {
            val intent = Intent(
                this,
                CadastroLivroActivity::class.java
            )
            startActivity(intent)
        }

        val buttonFavoritos =
            findViewById<Button>(R.id.buttonFavoritos)
        buttonFavoritos.setOnClickListener {
            val intent = Intent(
                this,
                FavoritosActivity::class.java
            )
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    // Busca os livros salvos no SQLite e atualiza o RecyclerView.
    private fun carregarLivros() {
        val livros = livroDAO.listarTodos()
        livroAdapter.atualizarLista(livros)
    }
    // Atualiza a lista sempre que a MainActivity volta a ficar visível.
    override fun onResume() {
        super.onResume()
        carregarLivros()
    }
}