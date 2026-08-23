package com.example.mybooks.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    // Pesquisa livros na Google Books API utilizando a chave do aplicativo.
    @GET("volumes")
    fun buscarLivros(
        @Query("q") pesquisa: String,
        @Query("key") apiKey: String,
        @Query("maxResults") maxResults: Int = 10
    ): Call<GoogleBooksResponse>
}

