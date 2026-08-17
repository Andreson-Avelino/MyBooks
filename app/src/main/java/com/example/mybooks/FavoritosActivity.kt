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
import android.view.View
import android.widget.TextView
class FavoritosActivity : AppCompatActivity() {

    // Mantém o acesso ao banco e o Adapter utilizado na lista de favoritos.
    private lateinit var livroDAO: LivroDAO
    private lateinit var livroAdapter: LivroAdapter

    private lateinit var textFavoritosVazio: TextView

    // Atualiza a lista sempre que a tela de favoritos volta a ficar visível.
    override fun onResume() {
        super.onResume()
        carregarFavoritos()
    }
    // Busca os favoritos e mostra uma mensagem quando nenhum livro está favoritado.
    private fun carregarFavoritos() {

        val favoritos =
            livroDAO.listarFavoritos()
        livroAdapter.atualizarLista(favoritos)
        if (favoritos.isEmpty()) {
            textFavoritosVazio.visibility =
                View.VISIBLE
        } else {
            textFavoritosVazio.visibility =
                View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos)

        textFavoritosVazio =
            findViewById(R.id.textFavoritosVazio)

        // Configura o RecyclerView para exibir apenas os livros favoritos.
        livroDAO = LivroDAO(this)

        val recyclerFavoritos =
            findViewById<RecyclerView>(R.id.recyclerFavoritos)

        livroAdapter = LivroAdapter(emptyList()) { livro ->
            val intent = Intent(
                this,
                DetalhesLivroActivity::class.java
            )
            intent.putExtra("livro_id", livro.id)
            startActivity(intent)
        }
        recyclerFavoritos.layoutManager =
            LinearLayoutManager(this)
        recyclerFavoritos.adapter =
            livroAdapter

        val buttonInicioFavoritos =
            findViewById<Button>(R.id.buttonInicioFavoritos)
        buttonInicioFavoritos.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
        }

        val buttonCadastrarFavoritos =
            findViewById<Button>(R.id.buttonCadastrarFavoritos)

        buttonCadastrarFavoritos.setOnClickListener {

            val intent = Intent(
                this,
                CadastroLivroActivity::class.java
            )

            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}