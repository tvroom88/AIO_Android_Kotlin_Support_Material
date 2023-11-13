package com.example.camerax_1

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var mBitmap = (applicationContext as MyApplication).bitmap

        if(mBitmap!!.width > mBitmap?.height!!){
            mBitmap =  rotateTheImage(90, mBitmap)
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(mBitmap)
    }


    fun rotateTheImage(orientation: Int, oldBitmap: Bitmap): Bitmap? {
        val newBitmap: Bitmap
        if (orientation != 0) {
            val matrix = Matrix()
            matrix.postRotate(orientation.toFloat())
            newBitmap = Bitmap.createBitmap(
                oldBitmap,
                0,
                0,
                oldBitmap.width,
                oldBitmap.height,
                matrix,
                false
            )
            return newBitmap
        }
        return oldBitmap
    }
}