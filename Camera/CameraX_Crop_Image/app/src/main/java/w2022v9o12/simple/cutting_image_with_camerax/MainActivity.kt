package w2022v9o12.simple.cutting_image_with_camerax

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var imageCapture: ImageCapture
    private lateinit var guideline: ImageView

    private lateinit var cameraExecutor: ExecutorService

    private val ratio = AspectRatio.RATIO_16_9
    private lateinit var captureBtn: ImageView

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var mCustomPreview: PreviewView

    private var mX: Float = 0F
    private var mY: Float = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.frameLayout)
        captureBtn = findViewById(R.id.capture_btn)
        guideline = findViewById(R.id.guideline)
        constraintLayout = findViewById(R.id.constraintLayout)


        //스크린 화면을 기준으로 16:9를 맞출 생각이다. 그래서 동적으로 넣어주기 위한 부분
        frameLayout = findViewById(R.id.frameLayout)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        mCustomPreview = PreviewView(this)
        val frameLayoutParams: FrameLayout.LayoutParams?
        val flag: Boolean = (screenWidth / 9 * 16) < screenHeight

        if (flag) {
            frameLayoutParams = FrameLayout.LayoutParams(
                screenWidth,
                screenWidth / 9 * 16
            )
        } else {
            frameLayoutParams = FrameLayout.LayoutParams(
                screenHeight / 16 * 9,
                screenHeight
            )
        }

        mCustomPreview.layoutParams = frameLayoutParams
        frameLayout.addView(mCustomPreview)

        cameraExecutor = Executors.newSingleThreadExecutor()

        captureBtn.setOnClickListener {
            takeAndProcessImage()
        }


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        frameLayout.viewTreeObserver
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // 좌표 가져오기
                    mX = frameLayout.x
                    mY = frameLayout.y

                    frameLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
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
                    processImage(image.toBitmap())
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    // 캡처 에러 처리
                }
            })
    }

    private fun processImage(bitmap: Bitmap) {
        // 이미지 데이터 추출

        var mBitamp = bitmap
        // 안드로이드는 돌려서 나오기 때문에 수정
        if (bitmap.width > bitmap.height) {
            mBitamp = rotateTheImage(90, bitmap)
        }

        mBitamp =
            Bitmap.createScaledBitmap(mBitamp, mCustomPreview.width, mCustomPreview.height, false);

        val smallBitmap = cropImage(mBitamp, mCustomPreview, guideline)


        val app = application as MyApplication
        app.fullImageBitmap = mBitamp
        app.smallImageBitmap = smallBitmap

        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }

    private fun cropImage(bitmap: Bitmap, frame: View, reference: View): Bitmap {

        val xInitialPos = (reference.x - mX).toInt()
        val yInitialPos = (reference.y - mY).toInt()

        Log.d("position", "mX : " + mX);
        Log.d("position", "mY : " + mY);

        val bitmapFinal = Bitmap.createBitmap(
            bitmap,
            xInitialPos, yInitialPos, reference.width, reference.height
        )

        val stream = ByteArrayOutputStream()
        bitmapFinal.compress(
            Bitmap.CompressFormat.PNG,
            100,
            stream
        ) //100 is the best quality possibe
        return bitmapFinal
    }


    private fun rotateTheImage(orientation: Int, oldBitmap: Bitmap): Bitmap {
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(ratio)
                .build()
                .also {
                    it.setSurfaceProvider(mCustomPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(ratio)
                .build()


            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val aspectRatio = Rational(9, 16)

            val viewPort: ViewPort = ViewPort.Builder(
                aspectRatio,
                mCustomPreview.display.rotation
            ).setScaleType(ViewPort.FIT).build()


            val useCaseGroupBuilder: UseCaseGroup.Builder = UseCaseGroup.Builder()
                .setViewPort(viewPort)
                .addUseCase(preview)
                .addUseCase(imageCapture)


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val mCamera = cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    useCaseGroupBuilder.build()
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
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
}