package com.example.rxjavapractice.repository

import com.example.rxjavapractice.network.CountriesApi
import com.example.rxjavapractice.network.RetrofitInstance

class NetworkRepository {

    private val client = RetrofitInstance.getInstance().create(CountriesApi::class.java)

    suspend fun getCountries() = client.getCountries()
}