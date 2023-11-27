package w2022v9o12.simple.camerax_analysis.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import w2022v9o12.simple.camerax_analysis.MainApplication
import w2022v9o12.simple.camerax_analysis.R
import w2022v9o12.simple.camerax_analysis.model.EDocument
import w2022v9o12.simple.camerax_analysis.model.ImageUtil

class ResultActivity : AppCompatActivity() {

    private var ivPhoto: ImageView? = null
    private var tvResult: TextView? = null

    var eDocument: EDocument? = null
    lateinit var imageUtil: ImageUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        ivPhoto = findViewById(R.id.view_photo)
        tvResult = findViewById(R.id.text_result)
        val intent = intent
        eDocument = intent.getSerializableExtra("EDOCUMENT") as EDocument?

        imageUtil = ImageUtil()
        setResultToView(eDocument)

        val application = applicationContext as MainApplication
        application.nfcIsRunning = false
    }

    private fun setResultToView(eDocument: EDocument?) {
        val image: Bitmap? = imageUtil.scaleImage(eDocument?.getPersonDetails()?.faceImage)
        ivPhoto!!.setImageBitmap(image)
        var result = """
            ${"NAME: " + (eDocument?.getPersonDetails()?.name)}

            """.trimIndent()
        result += """
            ${"SURNAME: " + eDocument?.getPersonDetails()?.surname}

            """.trimIndent()
        result += """
            ${"PERSONAL NUMBER: " + eDocument?.getPersonDetails()?.personalNumber}

            """.trimIndent()
        result += """
            ${"GENDER: " + eDocument?.getPersonDetails()?.gender}

            """.trimIndent()
        result += """
            ${"BIRTH DATE: " + eDocument?.getPersonDetails()?.birthDate}

            """.trimIndent()
        result += """
            ${"EXPIRY DATE: " + eDocument?.getPersonDetails()?.expiryDate}

            """.trimIndent()
        result += """
            ${"SERIAL NUMBER: " + eDocument?.getPersonDetails()?.serialNumber}

            """.trimIndent()
        result += """
            ${"NATIONALITY: " + eDocument?.getPersonDetails()?.nationality}

            """.trimIndent()

        result += """
            ${"ISSUER AUTHORITY: " + eDocument?.getPersonDetails()?.issuerAuthority}

            """.trimIndent()


        tvResult!!.text = result
    }

}