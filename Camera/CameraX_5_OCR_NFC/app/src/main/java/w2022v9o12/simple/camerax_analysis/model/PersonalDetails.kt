package w2022v9o12.simple.camerax_analysis.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.Serializable

class PersonDetails : Serializable {
    var name: String? = null
    var surname: String? = null
    var personalNumber: String? = null
    var gender: String? = null
    var birthDate: String? = null
    var expiryDate: String? = null
    var serialNumber: String? = null
    var nationality: String? = null
    var issuerAuthority: String? = null
    private lateinit var faceImageByteArray: ByteArray
    var faceImage: Bitmap?
        get() = byteArrayToBitmap(faceImageByteArray)
        set(faceImage) {
            val stream = ByteArrayOutputStream()
            faceImage!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
            faceImageByteArray = stream.toByteArray()
        }
    var faceImageBase64: String? = null
    var portraitImage: Bitmap? = null
    var portraitImageBase64: String? = null
    var signature: Bitmap? = null
    var signatureBase64: String? = null
    var fingerprints: List<Bitmap>? = null

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
