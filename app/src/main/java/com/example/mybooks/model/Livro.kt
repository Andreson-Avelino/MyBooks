package com.example.mybooks.model

data class Livro(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val sinopse: String,
    val favorito: Boolean = false
)