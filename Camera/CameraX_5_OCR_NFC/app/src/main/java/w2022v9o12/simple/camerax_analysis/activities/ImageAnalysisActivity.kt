package w2022v9o12.simple.camerax_analysis.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.sf.scuba.data.Gender
import org.jmrtd.lds.icao.MRZInfo
import w2022v9o12.simple.camerax_analysis.MainApplication
import w2022v9o12.simple.camerax_analysis.databinding.ActivityImageAnalysisBinding
import w2022v9o12.simple.camerax_analysis.model.MRZResult
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Pattern

class ImageAnalysisActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityImageAnalysisBinding

    // CameraX 관련 필드들
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private val mRatio = AspectRatio.RATIO_16_9
    private lateinit var cameraExecutor: ExecutorService

    // OCR 관련 필드
    private var textRecognizer: TextRecognizer? = null

    // 여권 MRZ를 판단하는 정규식
    val TD3_LINE1_REGEX = "P[A-Z<]([A-Z]{3})([A-Z<]*[A-Z])<<([A-Z<]*[A-Z])<*".toRegex()
    val TD3_LINE2_REGEX =
        "([A-Z0-9<]{9})[0-9]([A-Z]{3})(([0-9]{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))[0-9]([MF<])(([0-9]{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))[0-9]([A-Z0-9<]{14})[0-9]{2}".toRegex()

    val TD3_LINE1_REGEX_STR = "P[A-Z<]([A-Z]{3})([A-Z<]*[A-Z])<<([A-Z<]*[A-Z])<*"
    val TD3_LINE2_REGEX_STR =
        "([A-Z0-9<]{9})[0-9]([A-Z]{3})(([0-9]{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))[0-9]([MF<])(([0-9]{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1]))[0-9]([A-Z0-9<]{14})[0-9]{2}"

    private var lineTexts = arrayOfNulls<String>(2)

    private val MRZ_RESULT = "MRZ_RESULT"

    private lateinit var mBitmap: Bitmap

    private var count = 0

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityImageAnalysisBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        cameraExecutor = Executors.newSingleThreadExecutor()

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(mRatio)
                .build()

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(mRatio)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis!!.setAnalyzer(cameraExecutor, setAnalysis())

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    private fun setAnalysis(): ImageAnalysis.Analyzer {
        @ExperimentalGetImage
        val analysis = ImageAnalysis.Analyzer {
            recognizeText(it)
        }
        return analysis
    }

    private fun recognizeText(imageProxy: ImageProxy) {
        @ExperimentalGetImage
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            // [START run_detector]
            textRecognizer!!.process(image)
                .addOnSuccessListener { visionText ->
                    run {
                        processTextBlock(visionText)
                        mBitmap = imageProxy.toBitmap()
                    }
                }
                .addOnCompleteListener { imageProxy.close() }
                .addOnFailureListener { e ->
                    Log.d(
                        TAG,
                        "Text detection failed.$e"
                    )
                }
            // [END run_detector]
        }
    }


    private fun processTextBlock(result: Text) {

        var curText: String = ""
        val blocks: List<TextBlock> = result.textBlocks

        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    curText += elements[k].text
                }
                lineTexts[0] = lineTexts[1]
                lineTexts[1] = curText
                parseMRZ()
                curText = ""
            }
        }
    }

    private fun parseMRZ() {
        if (lineTexts.size != 2 || lineTexts[1] == null || lineTexts[0] == null) {
            return
        }

        val mrzResult = MRZResult()

        if (!lineTexts[0]!!.matches(TD3_LINE1_REGEX) || !lineTexts[1]!!.matches(TD3_LINE2_REGEX)) {
            if (lineTexts[0]!!.matches(TD3_LINE2_REGEX) && lineTexts[1]!!.matches(TD3_LINE1_REGEX)) {
                lineTexts = arrayOf(lineTexts[1], lineTexts[0])
                Log.d("parseTD3parseTD3", lineTexts[0] + " ---- " + lineTexts[1])
            } else {
                return
            }
        }

        mrzResult.mrzText = """
            ${lineTexts[0]}
            ${lineTexts[1]}
            """.trimIndent()

        mrzResult.isParsed = true
        mrzResult.isVerified = true
        mrzResult.docType = "passport"

        var pattern = Pattern.compile(TD3_LINE1_REGEX_STR)
        var matcher = pattern.matcher(lineTexts[0])
        if (matcher.find()) {
            mrzResult.issuer = matcher.group(1)
            mrzResult.surname = matcher.group(2)?.replace('<', ' ')
            mrzResult.givenName = matcher.group(3)?.replace('<', ' ')

            Log.d("PassportMRZ", "" + mrzResult.issuer)
            Log.d("PassportMRZ", "" + mrzResult.surname)
            Log.d("PassportMRZ", "" + mrzResult.givenName)

        } else {
            Log.d("PassportMRZ", "PassportMRZ Error")
            return
        }

        //line2

        //line2
        pattern = Pattern.compile(TD3_LINE2_REGEX_STR)
        matcher = pattern.matcher(lineTexts[1])
        if (matcher.find()) {
            mrzResult.docId = matcher.group(1).replace("<", "")

            if (!verifyString(matcher.group(1), lineTexts[1]!![9])) {
                mrzResult.isVerified = false   //check digital of document number
            }
            mrzResult.nationality = matcher.group(2)
            mrzResult.dateOfBirth =
                matcher.group(4) + "-" + matcher.group(5) + "-" + matcher.group(6)

            if (!verifyString(matcher.group(3), lineTexts[1]!![19])) {
                mrzResult.isVerified = false //check digital of birth date
            }
            mrzResult.dateOfBirth = matcher.group(4) + matcher.group(5) + matcher.group(6)
            mrzResult.gender = matcher.group(7)
            mrzResult.dateOfExpiration =
                matcher.group(9) + "-" + matcher.group(10) + "-" + matcher.group(11)

            if (!verifyString(matcher.group(8), lineTexts[1]!![27])) {
                mrzResult.isVerified = false  //check digital of expiration date
            }
            mrzResult.dateOfExpiration = matcher.group(9) + matcher.group(10) + matcher.group(11)

            if (!verifyString(matcher.group(12), lineTexts[1]!![42]) ||
                !verifyString(
                    lineTexts[1]!!.substring(0, 10) + lineTexts[1]!!.substring(
                        13,
                        20
                    ) + lineTexts[1]!!.substring(21, 43), lineTexts[1]!![43]
                )
            ) {
                mrzResult.isVerified = false //check digital of optional data and all
            }
        } else {
            return
        }

        Log.d("MRZ_Result", mrzResult.issuer!!)
        Log.d("MRZ_Result", mrzResult.surname!!)
        Log.d("MRZ_Result", mrzResult.givenName!!)
        Log.d("MRZ_Result", mrzResult.docId!!)
        Log.d("MRZ_Result", mrzResult.mrzText!!)
        Log.d("MRZ_Result", mrzResult.nationality!!)
        Log.d("MRZ_Result", mrzResult.dateOfBirth!!)
        Log.d("MRZ_Result", mrzResult.gender!!)
        Log.d("MRZ_Result", mrzResult.dateOfExpiration!!)

        val mrzInfo = buildTempMrz(mrzResult.docId, mrzResult.dateOfBirth, mrzResult.dateOfExpiration)
        if (isMrzValid(mrzInfo!!) && count < 1) {
            val intent = Intent(this, NfcScanActivity::class.java)
            intent.putExtra(MRZ_RESULT, mrzInfo)
            val application: MainApplication = applicationContext as MainApplication

            if(mBitmap.width > mBitmap.height){
                mBitmap = rotateTheImage(90, mBitmap)
            }
            application.mBitmap = mBitmap
            startActivity(intent)
            finish()
            count++
        }
    }

    private fun buildTempMrz(
        documentNumber: String?,
        dateOfBirth: String?,
        expiryDate: String?
    ): MRZInfo? {
        var mrzInfo: MRZInfo? = null
        try {
            mrzInfo = MRZInfo(
                "P", "NNN", "", "", documentNumber,
                "NNN", dateOfBirth, Gender.UNSPECIFIED, expiryDate, ""
            )
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "MRZInfo error : " + e.localizedMessage)
        }
        return mrzInfo
    }

    private fun verifyString(s: String, c: Char): Boolean {
        return if (c < '0' || c > '9') {
            false
        } else compute(s) == c.toString().toInt()
    }

    private fun compute(source: String): Int {
        val s = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val w = intArrayOf(7, 3, 1)
        var c = 0
        for (i in 0 until source.length) {
            if (source[i] == '<') continue
            c += s.indexOf(source[i]) * w[i % 3]
        }
        c %= 10
        return c
    }


    private fun isMrzValid(mrzInfo: MRZInfo): Boolean {
        return mrzInfo.documentNumber != null && mrzInfo.documentNumber.length >= 8 && mrzInfo.dateOfBirth != null && mrzInfo.dateOfBirth.length == 6 && mrzInfo.dateOfExpiry != null && mrzInfo.dateOfExpiry.length == 6
    }

    private fun rotateTheImage(orientation: Int, oldBitmap: Bitmap): Bitmap {
        val newBitmap = if (orientation != 0) {
            val matrix = Matrix()
            matrix.postRotate(orientation.toFloat())
            Bitmap.createBitmap(
                oldBitmap,
                0,
                0,
                oldBitmap.width,
                oldBitmap.height,
                matrix,
                false
            )
        } else {
            oldBitmap
        }
        return newBitmap
    }


    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}