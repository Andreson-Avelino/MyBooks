package com.example.mybooks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybooks.R
import com.example.mybooks.model.Livro

class LivroAdapter(
    private var livros: List<Livro>,
    private val onClickLivro: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    // Guarda as referências dos componentes visuais de cada item da lista.
    class LivroViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val titulo: TextView =
            itemView.findViewById(R.id.textTituloLivro)
        val autor: TextView =
            itemView.findViewById(R.id.textAutorLivro)
        val favorito: ImageView =
            itemView.findViewById(R.id.imageFavorito)
    }
    // Cria visualmente cada item usando o layout item_livro.xml.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LivroViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_livro,
                parent,
                false
            )
        return LivroViewHolder(view)
    }

    // Preenche cada item da lista com os dados do livro correspondente.
    override fun onBindViewHolder(
        holder: LivroViewHolder,
        position: Int
    ) {
        val livro = livros[position]

        holder.titulo.text = livro.titulo
        holder.autor.text = livro.autor
        holder.favorito.isSelected = livro.favorito
        holder.itemView.setOnClickListener {
            onClickLivro(livro)
        }
    }
    // Informa ao RecyclerView quantos livros existem na lista.
    override fun getItemCount(): Int {
        return livros.size
    }
    // Atualiza os livros exibidos quando os dados forem alterados.
    fun atualizarLista(novaLista: List<Livro>) {
        livros = novaLista
        notifyDataSetChanged()
    }
}