package com.example.rxjavaexample.views.coin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rxjavaexample.R

class CoinActivity : AppCompatActivity() {

    private lateinit var coinViewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        coinViewModel = ViewModelProvider(this)[CoinViewModel::class.java]
        coinViewModel.getCoinsList()


        coinViewModel.countries.observe(this, Observer {
           for(a in it.data){
               Log.d("aaaaaaaaaa", a.key)
           }
        })
    }
}