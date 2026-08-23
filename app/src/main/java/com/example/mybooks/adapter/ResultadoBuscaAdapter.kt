package com.example.mybooks.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybooks.R
import com.example.mybooks.api.BookItem

class ResultadoBuscaAdapter(
    private var resultados: List<BookItem>,
    private val onClickLivro: (BookItem) -> Unit
) : RecyclerView.Adapter<ResultadoBuscaAdapter.ResultadoViewHolder>() {

    // Guarda os componentes visuais utilizados para exibir cada livro encontrado.
    class ResultadoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val capa: ImageView =
            itemView.findViewById(R.id.imageCapaBusca)

        val titulo: TextView =
            itemView.findViewById(R.id.textTituloBusca)

        val autor: TextView =
            itemView.findViewById(R.id.textAutorBusca)
    }

    // Cria cada item visual usando o layout dos resultados da pesquisa.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultadoViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_resultado_busca,
                parent,
                false
            )

        return ResultadoViewHolder(view)
    }

    // Preenche cada resultado com título, autor e capa.
    override fun onBindViewHolder(
        holder: ResultadoViewHolder,
        position: Int
    ) {

        val livro = resultados[position]
        val info = livro.volumeInfo

        holder.titulo.text =
            info.title ?: ""

        holder.autor.text =
            info.authors?.joinToString(", ") ?: ""

        val urlCapa =
            info.imageLinks
                ?.thumbnail
                ?.replace("http://", "https://")

        Glide.with(holder.itemView.context)
            .load(urlCapa)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(holder.capa)

        holder.itemView.setOnClickListener {
            onClickLivro(livro)
        }
    }

    // Informa quantos resultados devem aparecer na lista.
    override fun getItemCount(): Int {
        return resultados.size
    }

    // Atualiza a lista quando uma nova pesquisa é realizada.
    fun atualizarResultados(
        novosResultados: List<BookItem>
    ) {
        resultados = novosResultados
        notifyDataSetChanged()
    }
}