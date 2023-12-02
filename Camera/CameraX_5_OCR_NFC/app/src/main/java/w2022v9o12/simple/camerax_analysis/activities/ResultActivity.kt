package w2022v9o12.simple.camerax_analysis.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import w2022v9o12.simple.camerax_analysis.MainApplication
import w2022v9o12.simple.camerax_analysis.databinding.ActivityResultBinding
import w2022v9o12.simple.camerax_analysis.model.EDocument
import w2022v9o12.simple.camerax_analysis.model.ImageUtil

class ResultActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityResultBinding

    private var eDocument: EDocument? = null
    private lateinit var imageUtil: ImageUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val intent = intent
        eDocument = intent.getSerializableExtra("EDOCUMENT") as EDocument?
        imageUtil = ImageUtil()
        setResultToView(eDocument)
    }

    override fun onStart() {
        super.onStart()
        (application as MainApplication).enableStopNFCReader(this)
    }


    override fun onPause() {
        super.onPause()
        (application as MainApplication).disableStopNFCReader(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setResultToView(eDocument: EDocument?) {
        val image: Bitmap? = imageUtil.scaleImage(eDocument?.getPersonDetails()?.faceImage)
        viewBinding.viewPhoto.setImageBitmap(image)

        viewBinding.textResult1.text = "NAME: " + eDocument?.getPersonDetails()?.name
        viewBinding.textResult2.text = "SURNAME: " + eDocument?.getPersonDetails()?.surname
        viewBinding.textResult3.text =
            "PERSONAL NUMBER: " + eDocument?.getPersonDetails()?.personalNumber


        var result = ""
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


        result += """
            ${"Name Of Holder: " + eDocument?.getAdditionalPersonDetail()?.nameOfHolder}

            """.trimIndent()


        result += """
            ${"Tax Or ExitRequirements: " + eDocument?.getAdditionalDocumentDetail()?.taxOrExitRequirements}

            """.trimIndent()

        viewBinding.textResult4.text = result
    }
}
