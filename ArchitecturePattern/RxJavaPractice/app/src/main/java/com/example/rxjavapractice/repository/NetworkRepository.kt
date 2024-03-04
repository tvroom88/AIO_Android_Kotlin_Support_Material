package com.example.rxjavapractice.repository

<<<<<<< HEAD
import com.example.rxjavapractice.network.CountriesApi
import com.example.rxjavapractice.network.RetrofitInstance

class NetworkRepository {

    private val client = RetrofitInstance.getInstance().create(CountriesApi::class.java)

    suspend fun getCountries() = client.getCountries()
=======
class NetworkRepository {
>>>>>>> 61b1f7696213fd0e8c836cd7963b966641208c78
}