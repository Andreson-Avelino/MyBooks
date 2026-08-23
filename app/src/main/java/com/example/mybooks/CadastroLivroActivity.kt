package com.example.mybooks

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mybooks.database.LivroDAO
import com.example.mybooks.model.Livro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mybooks.api.GoogleBooksResponse
import com.example.mybooks.api.RetrofitClient
class CadastroLivroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_cadastro_livro)

        // Localiza os campos da tela e prepara o botão para cadastrar um novo livro.
        val editTituloLivro =
            findViewById<EditText>(R.id.editTituloLivro)
        val editAutorLivro =
            findViewById<EditText>(R.id.editAutorLivro)
        val editSinopseLivro =
            findViewById<EditText>(R.id.editSinopseLivro)
        val buttonSalvarLivro =
            findViewById<Button>(R.id.buttonSalvarLivro)
        val livroDAO = LivroDAO(this)
        val editPesquisaLivro =
            findViewById<EditText>(R.id.editPesquisaLivro)
        val buttonBuscarLivro =
            findViewById<Button>(R.id.buttonBuscarLivro)

        // Pesquisa um livro na Google Books API e preenche os campos do cadastro.
        buttonBuscarLivro.setOnClickListener {

            val pesquisa =
                editPesquisaLivro.text.toString().trim()

            if (pesquisa.isEmpty()) {

                Toast.makeText(
                    this,
                    R.string.pesquisar_livro,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Obtém a chave configurada localmente e realiza a pesquisa na Google Books API.
            val apiKey = getString(R.string.google_books_api_key)

            RetrofitClient.googleBooksApi
                .buscarLivros(
                    pesquisa,
                    apiKey
                )
                .enqueue(

                    object : Callback<GoogleBooksResponse> {

                        override fun onResponse(
                            call: Call<GoogleBooksResponse>,
                            response: Response<GoogleBooksResponse>
                        ) {
                            if (response.isSuccessful) {
                                val livros =
                                    response.body()?.items

                                if (!livros.isNullOrEmpty()) {
                                    val livroEncontrado =
                                        livros.first().volumeInfo
                                    editTituloLivro.setText(
                                        livroEncontrado.title ?: ""
                                    )
                                    editAutorLivro.setText(
                                        livroEncontrado.authors
                                            ?.joinToString(", ")
                                            ?: ""
                                    )
                                    editSinopseLivro.setText(
                                        livroEncontrado.description
                                            ?: ""
                                    )
                                    Toast.makeText(
                                        this@CadastroLivroActivity,
                                        R.string.livro_encontrado,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {

                                    // Exibe o código HTTP para identificar problemas na comunicação com a API.
                                    Toast.makeText(
                                        this@CadastroLivroActivity,
                                        "Erro HTTP: ${response.code()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@CadastroLivroActivity,
                                    R.string.erro_busca_livro,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        override fun onFailure(
                            call: Call<GoogleBooksResponse>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                this@CadastroLivroActivity,
                                R.string.erro_busca_livro,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
        }

        // Valida os campos, cria um objeto Livro e salva os dados no banco SQLite.
        buttonSalvarLivro.setOnClickListener {

            val titulo = editTituloLivro.text.toString().trim()
            val autor = editAutorLivro.text.toString().trim()
            val sinopse = editSinopseLivro.text.toString().trim()

            if (titulo.isEmpty() || autor.isEmpty() || sinopse.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val livro = Livro(
                titulo = titulo,
                autor = autor,
                sinopse = sinopse
            )
            val idLivro = livroDAO.inserirLivro(livro)

            if (idLivro != -1L) {
                Toast.makeText(
                    this,
                    "Livro cadastrado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Erro ao cadastrar livro",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
}