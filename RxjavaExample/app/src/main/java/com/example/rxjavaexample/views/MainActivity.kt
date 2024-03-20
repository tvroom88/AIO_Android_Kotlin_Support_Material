package com.example.rxjavaexample.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.rxjavaexample.R
import com.example.rxjavaexample.views.coin.CoinActivity
import com.example.rxjavaexample.views.search.SearchExampleActivity

class MainActivity : AppCompatActivity() {

    private var simpleExBtn: Button? = null
    private var editTextExBtn: Button? = null
    private var coinListBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleExBtn = findViewById(R.id.simpleExBtn)
        simpleExBtn?.setOnClickListener {
            val intent = Intent(this, SimpleExampleActivity::class.java)
            startActivity(intent)
        }

        editTextExBtn = findViewById(R.id.editTextExBtn)
        editTextExBtn?.setOnClickListener {
            val intent = Intent(this, SearchExampleActivity::class.java)
            startActivity(intent)
        }

        coinListBtn = findViewById(R.id.coinListBtn)
        coinListBtn?.setOnClickListener {
            val intent = Intent(this, CoinActivity::class.java)
            startActivity(intent)
        }
    }
}