
package com.example.mybooks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybooks.database.LivroDAO

class DetalhesLivroActivity : AppCompatActivity() {

    // Armazena o estado atual de favorito do livro exibido.
    private var estaFavorito = false

    // Inicializa a tela, carrega o livro e configura seus botões.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_livro)

        val textNomeLivroDetalhes =
            findViewById<TextView>(R.id.textNomeLivroDetalhes)

        val textAutorDetalhes =
            findViewById<TextView>(R.id.textAutorDetalhes)

        val textSinopseDetalhes =
            findViewById<TextView>(R.id.textSinopseDetalhes)

        val buttonFavoritarDetalhes =
            findViewById<Button>(R.id.buttonFavoritarDetalhes)

        val buttonEditarLivro =
            findViewById<Button>(R.id.buttonEditarLivro)

        val buttonExcluirLivro =
            findViewById<Button>(R.id.buttonExcluirLivro)

        val livroId = intent.getIntExtra("livro_id", -1)
        val livroDAO = LivroDAO(this)
        val livro = livroDAO.buscarPorId(livroId)

        if (livro != null) {
            // Exibe as informações recuperadas do banco SQLite.
            textNomeLivroDetalhes.text = livro.titulo
            textAutorDetalhes.text = livro.autor
            textSinopseDetalhes.text = livro.sinopse

            estaFavorito = livro.favorito
            atualizarTextoFavorito(
                buttonFavoritarDetalhes,
                estaFavorito
            )
            // Abre a tela de cadastro para editar o livro existente.
            buttonEditarLivro.setOnClickListener {
                val intentEdicao = Intent(
                    this,
                    CadastroLivroActivity::class.java
                )
                intentEdicao.putExtra(
                    "livro_id_edicao",
                    livro.id
                )
                startActivity(intentEdicao)
            }
            // Alterna o favorito somente após confirmar a gravação.
            buttonFavoritarDetalhes.setOnClickListener {
                val novoEstado = !estaFavorito
                val resultado = livroDAO.atualizarFavorito(
                    livro.id,
                    novoEstado
                )
                if (resultado > 0) {
                    estaFavorito = novoEstado
                    atualizarTextoFavorito(
                        buttonFavoritarDetalhes,
                        estaFavorito
                    )
                    val mensagem = if (estaFavorito) {
                        R.string.livro_adicionado_favoritos
                    } else {
                        R.string.livro_removido_favoritos
                    }
                    Toast.makeText(
                        this,
                        mensagem,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Não foi possível atualizar o favorito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Solicita confirmação antes de excluir o livro.
            buttonExcluirLivro.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle(R.string.titulo_excluir_livro)
                    .setMessage(R.string.confirmacao_excluir_livro)
                    .setNegativeButton(R.string.cancelar, null)
                    .setPositiveButton(R.string.excluir) { _, _ ->
                        val resultado = livroDAO.excluirLivro(
                            livro.id
                        )
                        if (resultado > 0) {

                            Toast.makeText(
                                this,
                                R.string.livro_excluido_sucesso,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                R.string.erro_excluir_livro,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .show()
            }
        } else {
            // Encerra a tela quando o livro não é encontrado.
            Toast.makeText(
                this,
                R.string.livro_nao_encontrado,
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        // Ajusta o conteúdo para respeitar as barras do Android.
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
    // Recarrega os dados do livro ao retornar da tela de edição.
    override fun onResume() {
        super.onResume()

        val livroId = intent.getIntExtra("livro_id", -1)
        if (livroId == -1) return
        val livroAtualizado = LivroDAO(this).buscarPorId(livroId)
            ?: return

        findViewById<TextView>(
            R.id.textNomeLivroDetalhes
        ).text = livroAtualizado.titulo

        findViewById<TextView>(
            R.id.textAutorDetalhes
        ).text = livroAtualizado.autor

        findViewById<TextView>(
            R.id.textSinopseDetalhes
        ).text = livroAtualizado.sinopse

        // Sincroniza o favorito com o estado salvo no SQLite.
        estaFavorito = livroAtualizado.favorito
        atualizarTextoFavorito(
            findViewById(R.id.buttonFavoritarDetalhes),
            estaFavorito
        )
    }
    // Define o texto do botão conforme o estado de favorito.
    private fun atualizarTextoFavorito(
        button: Button,
        favorito: Boolean
    ) {
        if (favorito) {
            button.setText(R.string.desfavoritar)
        } else {
            button.setText(R.string.favoritar)
        }
    }
}
