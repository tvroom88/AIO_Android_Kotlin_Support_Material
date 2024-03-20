package com.example.rxjavapractice.network

import com.example.rxjavapractice.dataModel.CountryModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://raw.githubusercontent.com/"

    private var instance: RetrofitInstance? = null

    private val api: CountriesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }

    fun getInstance(): RetrofitInstance? {
        if(instance == null){
            instance = RetrofitInstance
        }
        return instance
    }

    fun getCountries(): Single<List<CountryModel>> {
        return api.getCountries()
    }
}
