package w2022v9o12.simple.cutting_image_with_camerax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ResultActivity : AppCompatActivity() {

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        imageView = findViewById(R.id.imageView)

        val app = application as MyApplication
        imageView.setImageBitmap(app.bitmap)


    }
}