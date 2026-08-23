package com.example.mybooks.api

    // Representa a resposta principal recebida da Google Books API.
    data class GoogleBooksResponse(
        val totalItems: Int = 0,
        val items: List<BookItem>? = null
    )

    // Representa cada livro retornado pela pesquisa.
    data class BookItem(
        val id: String,
        val volumeInfo: VolumeInfo
    )

    // Contém as informações bibliográficas que vamos usar no MyBooks.
    data class VolumeInfo(
        val title: String? = null,
        val authors: List<String>? = null,
        val description: String? = null,
        val industryIdentifiers: List<IndustryIdentifier>? = null,
        val imageLinks: ImageLinks? = null
    )

    // Representa identificadores como ISBN-10 e ISBN-13.
    data class IndustryIdentifier(
        val type: String? = null,
        val identifier: String? = null
    )

// Contém os endereços das imagens de capa retornadas pela API.
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)