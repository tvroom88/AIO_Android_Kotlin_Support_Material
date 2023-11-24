package w2022v9o12.simple.camerax_analysis.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import w2022v9o12.simple.camerax_analysis.R
import w2022v9o12.simple.camerax_analysis.model.EDocument

class ResultActivity : AppCompatActivity() {

    private var ivPhoto: ImageView? = null
    private var tvResult: TextView? = null

    var eDocument: EDocument? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        ivPhoto = findViewById(R.id.view_photo)
        tvResult = findViewById(R.id.text_result)
        val intent = intent
//        eDocument = intent.getSerializableExtra(EDOCUMENT) as EDocument?
//        setResultToView(eDocument)
    }

//    private fun setResultToView(eDocument: EDocument?) {
//        val image: Bitmap = ImageUtil.scaleImage(eDocument.getPersonDetails().getFaceImage())
//        ivPhoto!!.setImageBitmap(image)
//        var result = """
//            ${"NAME: " + eDocument.getPersonDetails().getName()}
//
//            """.trimIndent()
//        result += """
//            ${"SURNAME: " + eDocument.getPersonDetails().getSurname()}
//
//            """.trimIndent()
//        result += """
//            ${"PERSONAL NUMBER: " + eDocument.getPersonDetails().getPersonalNumber()}
//
//            """.trimIndent()
//        result += """
//            ${"GENDER: " + eDocument.getPersonDetails().getGender()}
//
//            """.trimIndent()
//        result += """
//            ${"BIRTH DATE: " + eDocument.getPersonDetails().getBirthDate()}
//
//            """.trimIndent()
//        result += """
//            ${"EXPIRY DATE: " + eDocument.getPersonDetails().getExpiryDate()}
//
//            """.trimIndent()
//        result += """
//            ${"SERIAL NUMBER: " + eDocument.getPersonDetails().getSerialNumber()}
//
//            """.trimIndent()
//        result += """
//   ${"NATIONALITY: " + eDocument.getPersonDetails().getNationality()}
//
//            """.trimIndent()
//        result += """
//            ${"DOC TYPE: " + eDocument.getDocType().name()}
//
//            """.trimIndent()
//        result += """
//            ${"ISSUER AUTHORITY: " + eDocument.getPersonDetails().getIssuerAuthority()}
//
//            """.trimIndent()
//        tvResult!!.text = result
//    }

}