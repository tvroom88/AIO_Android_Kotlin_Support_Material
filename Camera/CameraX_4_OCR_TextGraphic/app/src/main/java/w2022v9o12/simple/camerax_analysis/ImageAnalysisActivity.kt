package w2022v9o12.simple.camerax_analysis

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import w2022v9o12.simple.camerax_analysis.databinding.ActivityImageAnalysisBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageAnalysisActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityImageAnalysisBinding
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null

    private val mRatio = AspectRatio.RATIO_16_9

    private lateinit var cameraExecutor: ExecutorService

    private var textRecognizer: TextRecognizer? = null

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
                    processTextBlock(
                        visionText,
                        mediaImage.cropRect
                    )
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


    private fun processTextBlock(result: Text, rect: Rect) {
        var curText: String = ""

        viewBinding.graphicOverlayFinder.clear()
        result.textBlocks.forEach {
            val customText = GraphicOverlay.CustomText(it, rect)
            viewBinding.graphicOverlayFinder.add(customText)
            viewBinding.textView.text = it.text

            val lines = it.lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    curText += elements[k].text
                }
            }
        }
        viewBinding.textView.text = curText
        curText = ""

        viewBinding.graphicOverlayFinder.postInvalidate()

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