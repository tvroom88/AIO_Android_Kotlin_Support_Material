package w2022v9o12.simple.camerax_analysis

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import w2022v9o12.simple.camerax_analysis.databinding.ActivityImageCaptureBinding
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageCaptureActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityImageCaptureBinding
    private var imageCapture: ImageCapture? = null

    private val mRatio = AspectRatio.RATIO_16_9

    private lateinit var cameraExecutor: ExecutorService

    private var textRecognizer: TextRecognizer? = null

    private lateinit var handler: Handler

    private lateinit var mConext: Context

    private var mFlag = false

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
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
        viewBinding = ActivityImageCaptureBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mConext = this

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        cameraExecutor = Executors.newSingleThreadExecutor()

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        viewBinding.capture.setOnClickListener {
            if (mFlag) { //imageView가 보이는거면 true 아니면 false
                viewBinding.capture.text = "사진 찍기"
                mFlag = false
                viewBinding.imageView.visibility = View.INVISIBLE
            } else {
                takeAndProcessImage()
                viewBinding.capture.text = "사진 다시 찍기"
                mFlag = true
                viewBinding.imageView.visibility = View.VISIBLE
            }
        }

        // UI는 Main Thread에서만 바로 바꿀수 있어서 Handler 사용해서 교체
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                // UI 변경 작업을 여기에 수행
                val mBitmap = msg.obj as Bitmap
                viewBinding.imageView.setImageBitmap(mBitmap)
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(mRatio)
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }


            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(mRatio)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
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
                .addOnSuccessListener { visionText -> processTextBlock(visionText) }
                .addOnCompleteListener { imageProxy.close() }
                .addOnFailureListener { e ->
                    Log.d(
                        ImageCaptureActivity.TAG,
                        "Text detection failed.$e"
                    )
                }
            // [END run_detector]
            val message = handler.obtainMessage()
            var mBitmap = imageProxy.toBitmap()
            if (mBitmap.width > mBitmap.height) {
                mBitmap = rotateTheImage(90, mBitmap)
            }
            message.obj = mBitmap
            handler.sendMessage(message)
//            }
        }
    }

    private fun processTextBlock(result: Text) {
        val blocks = result.textBlocks
        var curText: String = ""
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    curText += elements[k].text
                }
                Log.d("curText : ", curText)
            }
        }
        viewBinding.textView.text = curText
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

    private fun takeAndProcessImage() {
        val imageCapture = imageCapture
        val newExecutor: Executor = Executors.newSingleThreadExecutor()

        // 이미지 캡처 콜백 등록
        imageCapture!!.takePicture(
            newExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // 이미지 데이터 추출 및 처리
                    recognizeText(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    // 캡처 에러 처리
                }
            })
    }

    private fun rotateTheImage(orientation: Int, oldBitmap: Bitmap): Bitmap {
        var newBitmap: Bitmap? = null
        newBitmap = if (orientation != 0) {
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