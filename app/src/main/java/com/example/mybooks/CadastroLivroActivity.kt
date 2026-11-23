
package com.example.mybooks

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybooks.adapter.ResultadoBuscaAdapter
import com.example.mybooks.api.GoogleBooksResponse
import com.example.mybooks.api.RetrofitClient
import com.example.mybooks.database.LivroDAO
import com.example.mybooks.model.Livro
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroLivroActivity : AppCompatActivity() {
    private lateinit var resultadoBuscaAdapter: ResultadoBuscaAdapter
    // Identifica o livro em edição; -1 indica um novo cadastro.
    private var livroIdEdicao = -1
    // Inicializa a tela e configura o cadastro, a edição e a pesquisa online.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_livro)

        val editTituloLivro =
            findViewById<EditText>(R.id.editTituloLivro)

        val editAutorLivro =
            findViewById<EditText>(R.id.editAutorLivro)

        val editSinopseLivro =
            findViewById<EditText>(R.id.editSinopseLivro)

        val buttonSalvarLivro =
            findViewById<Button>(R.id.buttonSalvarLivro)

        val editPesquisaLivro =
            findViewById<EditText>(R.id.editPesquisaLivro)

        val buttonBuscarLivro =
            findViewById<Button>(R.id.buttonBuscarLivro)

        val recyclerResultadosBusca =
            findViewById<RecyclerView>(R.id.recyclerResultadosBusca)

        val livroDAO = LivroDAO(this)

        // Verifica se a tela foi aberta para cadastrar ou editar um livro.
        livroIdEdicao = intent.getIntExtra(
            "livro_id_edicao",
            -1
        )

        if (livroIdEdicao != -1) {
            val livroExistente = livroDAO.buscarPorId(
                livroIdEdicao
            )
            if (livroExistente != null) {
                // Preenche os campos com os dados já salvos no SQLite.
                editTituloLivro.setText(livroExistente.titulo)
                editAutorLivro.setText(livroExistente.autor)
                editSinopseLivro.setText(livroExistente.sinopse)

                buttonSalvarLivro.text = "Salvar Alterações"

            } else {

                Toast.makeText(
                    this,
                    "Livro não encontrado para edição",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
                return
            }
        }

        // Configura a seleção dos livros encontrados na Google Books API.
        resultadoBuscaAdapter =
            ResultadoBuscaAdapter(emptyList()) { livroSelecionado ->

                val info = livroSelecionado.volumeInfo
                editTituloLivro.setText(info.title ?: "")

                editAutorLivro.setText(
                    info.authors?.joinToString(", ") ?: ""
                )
                editSinopseLivro.setText(
                    info.description ?: ""
                )
                recyclerResultadosBusca.visibility = View.GONE
                Toast.makeText(
                    this,
                    R.string.livro_encontrado,
                    Toast.LENGTH_SHORT
                ).show()
            }

        recyclerResultadosBusca.layoutManager =
            LinearLayoutManager(this)

        recyclerResultadosBusca.adapter =
            resultadoBuscaAdapter

        // Pesquisa livros online e permite continuar manualmente se falhar.
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

            val apiKey =
                getString(R.string.google_books_api_key)

            recyclerResultadosBusca.visibility = View.GONE
            buttonBuscarLivro.isEnabled = false

            RetrofitClient.googleBooksApi
                .buscarLivros(pesquisa, apiKey)
                .enqueue(object : Callback<GoogleBooksResponse> {

                    override fun onResponse(
                        call: Call<GoogleBooksResponse>,
                        response: Response<GoogleBooksResponse>
                    ) {

                        if (isFinishing || isDestroyed) return

                        buttonBuscarLivro.isEnabled = true

                        if (response.isSuccessful) {

                            val livros = response.body()?.items

                            if (!livros.isNullOrEmpty()) {

                                resultadoBuscaAdapter
                                    .atualizarResultados(livros)

                                recyclerResultadosBusca.visibility =
                                    View.VISIBLE

                            } else {

                                recyclerResultadosBusca.visibility =
                                    View.GONE

                                mostrarAvisoCadastroManual(
                                    "Livro não encontrado",
                                    "Nenhum livro foi encontrado na pesquisa. " +
                                            "Você pode preencher os dados manualmente " +
                                            "e salvar o livro."
                                )
                            }

                        } else {

                            recyclerResultadosBusca.visibility =
                                View.GONE

                            mostrarAvisoCadastroManual(
                                "Pesquisa indisponível",
                                "Não foi possível concluir a pesquisa " +
                                        "(erro HTTP ${response.code()}). " +
                                        "Você pode preencher os dados manualmente."
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<GoogleBooksResponse>,
                        t: Throwable
                    ) {

                        if (isFinishing || isDestroyed) return

                        buttonBuscarLivro.isEnabled = true
                        recyclerResultadosBusca.visibility = View.GONE

                        val mensagem = if (t is IOException) {

                            "Não foi possível conectar à pesquisa online. " +
                                    "Verifique sua internet. As funções locais " +
                                    "continuam disponíveis e você pode preencher " +
                                    "os dados manualmente."

                        } else {

                            "Ocorreu um problema na pesquisa online. " +
                                    "Você ainda pode preencher os dados manualmente."
                        }

                        mostrarAvisoCadastroManual(
                            "Pesquisa indisponível",
                            mensagem
                        )
                    }
                })
        }

        // Salva um novo livro ou atualiza o livro existente no SQLite.
        buttonSalvarLivro.setOnClickListener {

            val titulo =
                editTituloLivro.text.toString().trim()

            val autor =
                editAutorLivro.text.toString().trim()

            val sinopse =
                editSinopseLivro.text.toString().trim()

            if (titulo.isEmpty()) {

                editTituloLivro.error =
                    "Informe o título do livro"

                editTituloLivro.requestFocus()
                return@setOnClickListener
            }

            if (livroIdEdicao != -1) {

                // Atualiza os dados mantendo o ID e o favorito existentes.
                val resultado = livroDAO.atualizarInformacoes(
                    livroIdEdicao,
                    titulo,
                    autor,
                    sinopse
                )

                if (resultado > 0) {

                    Toast.makeText(
                        this,
                        "Livro atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Erro ao atualizar livro",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                // Insere um novo livro quando não existe ID de edição.
                val livro = Livro(
                    titulo = titulo,
                    autor = autor,
                    sinopse = sinopse
                )

                val idLivro = livroDAO.inserirLivro(livro)

                if (idLivro != -1L) {

                    val mensagem =
                        if (autor.isEmpty() || sinopse.isEmpty()) {

                            "Livro salvo! Você poderá completar " +
                                    "as informações posteriormente."

                        } else {

                            "Livro cadastrado com sucesso!"
                        }

                    Toast.makeText(
                        this,
                        mensagem,
                        Toast.LENGTH_LONG
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
        }

        // Ajusta o conteúdo às barras de navegação do Android.
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
    // Exibe avisos da pesquisa sem bloquear o preenchimento manual.
    private fun mostrarAvisoCadastroManual(
        titulo: String,
        mensagem: String
    ) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("Entendi", null)
            .show()
    }
}
