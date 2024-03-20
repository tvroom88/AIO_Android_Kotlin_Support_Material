package com.example.rxjavaexample.network


import com.example.rxjavaexample.network.networkModel.CurrentPriceList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface Api {

    // https://api.bithumb.com/public/ticker/ALL_KRW

    // public/ticker/ALL_KRW
    @GET("public/ticker/ALL_KRW")
    fun getCurrentCoinList() : Single<CurrentPriceList>
}
