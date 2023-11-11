package com.example.camerax_1

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Intent에서 Bitmap 추출
        val bitmap: Bitmap? = intent.getParcelableExtra("bitmap")

        if (bitmap != null) {
            // Bitmap을 사용하거나 표시
            // 예: ImageView에 표시
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(bitmap)
        }
    }
}