package w2022v9o12.simple.camerax_analysis


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import w2022v9o12.simple.camerax_analysis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.imageAnalysisButton.setOnClickListener{
            val intent = Intent(this, ImageAnalysisActivity::class.java)
            startActivity(intent)
        }

        viewBinding.imageCaptureButton.setOnClickListener{
            val intent = Intent(this, ImageCaptureActivity::class.java)
            startActivity(intent)
        }
    }
}