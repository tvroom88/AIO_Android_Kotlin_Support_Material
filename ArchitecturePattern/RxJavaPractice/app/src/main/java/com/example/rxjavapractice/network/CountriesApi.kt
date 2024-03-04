package com.example.rxjavapractice.network

import com.example.rxjavapractice.dataModel.CountryModel
import io.reactivex.Single

import retrofit2.http.GET

interface CountriesApi {
    @GET("DevTides/countries/master/countriesV2.json")
    fun getCountries(): Single<List<CountryModel>>
}