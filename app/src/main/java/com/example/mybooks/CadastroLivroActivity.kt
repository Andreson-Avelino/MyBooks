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