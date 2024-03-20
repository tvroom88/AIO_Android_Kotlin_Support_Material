package com.example.rxjavaexample.repository

import com.example.rxjavaexample.network.Api
import com.example.rxjavaexample.network.RetrofitInstance
import com.example.rxjavaexample.network.networkModel.CurrentPriceList
import io.reactivex.rxjava3.core.Single

class NetworkRepository {

    private val client = RetrofitInstance.getInstance().create(Api::class.java)

    fun getCurrentCoinList(): Single<CurrentPriceList> = client.getCurrentCoinList()

}