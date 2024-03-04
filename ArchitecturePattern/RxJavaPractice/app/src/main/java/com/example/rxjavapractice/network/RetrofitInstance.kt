package com.example.rxjavapractice.network

<<<<<<< HEAD
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://raw.githubusercontent.com/"

    private val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance(): Retrofit {
        return client
    }
=======
class RetrofitInstance {
>>>>>>> 61b1f7696213fd0e8c836cd7963b966641208c78
}