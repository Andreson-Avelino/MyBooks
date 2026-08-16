package com.example.mybooks

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybooks.database.LivroDAO

class DetalhesLivroActivity : AppCompatActivity() {

    // Carrega a tela de detalhes, busca o livro no banco e configura o botão de favorito.
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

        val livroId =
            intent.getIntExtra("livro_id", -1)

        val livroDAO =
            LivroDAO(this)

        val livro =
            livroDAO.buscarPorId(livroId)

        if (livro != null) {

            textNomeLivroDetalhes.text =
                livro.titulo

            textAutorDetalhes.text =
                livro.autor

            textSinopseDetalhes.text =
                livro.sinopse

            var estaFavorito =
                livro.favorito

            atualizarTextoFavorito(
                buttonFavoritarDetalhes,
                estaFavorito
            )

            // Alterna o estado de favorito e salva a alteração no SQLite.
            buttonFavoritarDetalhes.setOnClickListener {

                estaFavorito =
                    !estaFavorito

                val resultado =
                    livroDAO.atualizarFavorito(
                        livro.id,
                        estaFavorito
                    )

                if (resultado > 0) {

                    atualizarTextoFavorito(
                        buttonFavoritarDetalhes,
                        estaFavorito
                    )

                    if (estaFavorito) {

                        Toast.makeText(
                            this,
                            R.string.livro_adicionado_favoritos,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            R.string.livro_removido_favoritos,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } else {

            Toast.makeText(
                this,
                R.string.livro_nao_encontrado,
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
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

    // Define se o botão deve mostrar "Favoritar" ou "Desfavoritar".
    private fun atualizarTextoFavorito(
        button: Button,
        favorito: Boolean
    ) {

        if (favorito) {

            button.setText(
                R.string.desfavoritar
            )

        } else {

            button.setText(
                R.string.favoritar
            )
        }
    }
}