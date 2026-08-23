package com.example.mybooks.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL =
        "https://www.googleapis.com/books/v1/"

    // Cria uma única instância do Retrofit para acessar a Google Books API.
    val googleBooksApi: GoogleBooksApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(GoogleBooksApi::class.java)
    }
}