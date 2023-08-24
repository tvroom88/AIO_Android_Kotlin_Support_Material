package w2022v9o12.simple.cutting_image_with_camerax

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var imageCapture: ImageCapture
    private var imageAnalysis: ImageAnalysis? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private val recording: Recording? = null
    private lateinit var guideline: ImageView

    private lateinit var cameraExecutor: ExecutorService

    private val ratio = AspectRatio.RATIO_16_9

    private lateinit var viewFinder: PreviewView
    private lateinit var capture_btn: ImageButton

    private lateinit var constraintLayout: ConstraintLayout

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        viewFinder = findViewById(R.id.viewFinder)
        capture_btn = findViewById(R.id.capture_btn)
        guideline = findViewById(R.id.guideline)
        constraintLayout = findViewById(R.id.constraintLayout)

        capture_btn.setOnClickListener {
            takeAndProcessImage()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    //이미지를 저장하지 않고 데이터만 가져온다.
    private fun takeAndProcessImage() {
        val imageCapture = imageCapture
        val newExecutor: Executor = Executors.newSingleThreadExecutor()

        // 이미지 캡처 콜백 등록
        imageCapture.takePicture(
            newExecutor,
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // 이미지 데이터 추출 및 처리
                    processImage(image)

                    // 이미지 닫기
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    // 캡처 에러 처리
                }
            })
    }

    private fun processImage(image: ImageProxy) {
        // 이미지 데이터 추출
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val imageBitmap = cropImage(bitmap, viewFinder, guideline)
//        val imageBitmap = cropImage(bitmap, constraintLayout, guideline)

        val app = application as MyApplication

        app.bitmap = imageBitmap

        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)


        // Bitmap 사용 후 해제
        bitmap.recycle()
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
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()


            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun cropImage(bitmap: Bitmap, frame: View, reference: View): Bitmap {

        val mBitmap = rotateTheImage(90, bitmap)
        val heightOriginal = frame.height
        val widthOriginal = frame.width
        val heightFrame = reference.height
        val widthFrame = reference.width
        val leftFrame = reference.left
        val topFrame = reference.top
        val heightReal = mBitmap.height
        val widthReal = mBitmap.width
        val widthFinal = widthFrame * widthReal / widthOriginal
        val heightFinal = heightFrame * heightReal / heightOriginal
        val leftFinal = leftFrame * widthReal / widthOriginal
        val topFinal = topFrame * heightReal / heightOriginal


        val bitmapFinal = Bitmap.createBitmap(
            mBitmap,
            leftFinal, topFinal, widthFinal, heightFinal
        )
        val stream = ByteArrayOutputStream()
        bitmapFinal.compress(
            Bitmap.CompressFormat.PNG,
            100,
            stream
        ) //100 is the best quality possibe
        return bitmapFinal
    }


    fun rotateTheImage(orientation: Int, oldBitmap: Bitmap): Bitmap {
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