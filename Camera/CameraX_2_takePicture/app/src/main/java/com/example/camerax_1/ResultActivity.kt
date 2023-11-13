package com.example.camerax_1

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap((applicationContext as MyApplication).bitmap )
    }
}