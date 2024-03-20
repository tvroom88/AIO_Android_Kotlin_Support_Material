package com.example.rxjavaexample.views.coin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rxjavaexample.R
import com.example.rxjavaexample.repository.NetworkRepository

class CoinActivity : AppCompatActivity() {


//    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)
    }
}