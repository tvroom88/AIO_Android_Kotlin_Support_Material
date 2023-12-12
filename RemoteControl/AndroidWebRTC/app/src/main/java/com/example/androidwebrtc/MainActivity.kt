package com.example.androidwebrtc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidwebrtc.databinding.ActivityMainBinding
import com.example.androidwebrtc.utils.CaptureMode


/**
 *  해야할일:
 *  (1) 처음에 스크린 Sharing을 할지, video capture를 할지 선택하기
 *  (2) rtcClient?.initializeSurfaceView(localView) 초기화 하는 부분 잘 설정하기
 *  (3)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 권한들중 허가 안된 권한들
    private val notPermittedPermissions: MutableList<String> = mutableListOf()

    private lateinit var launcher: ActivityResultLauncher<Intent>

    private lateinit var mContext: Context
    private lateinit var mActivity: Activity

    private lateinit var captureMode: CaptureMode

    companion object {
        private const val PERMISSION = 100

        // 카메라, 오디오 권한
        private val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this
        mActivity = this

        binding.apply {
            enterBtn.setOnClickListener {
                if (checkPermission()) {
                    val intent = Intent(mContext, CallActivity::class.java)
                    intent.putExtra("username", binding.username.text.toString())
                    launcher.launch(intent)
                } else {
                    ActivityCompat.requestPermissions(
                        mActivity,
                        notPermittedPermissions.toTypedArray(), PERMISSION
                    );
                }
            }

            videoCaptureBtn.setOnClickListener {
                videoCaptureBtn.setBackgroundResource(R.drawable.capture_selected_btn_bg)
                screenCaptureBtn.setBackgroundResource(R.drawable.capture_unselected_btn_bg)
                captureMode = CaptureMode.VIDEO_CAPTURE
            }

            screenCaptureBtn.setOnClickListener {
                videoCaptureBtn.setBackgroundResource(R.drawable.capture_unselected_btn_bg)
                screenCaptureBtn.setBackgroundResource(R.drawable.capture_selected_btn_bg)
                captureMode = CaptureMode.SCREEN_CAPTURE
            }
        }



        launcher = registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                //SubActivity에서 갖고온 Intent(It)
                val message = it.data?.getStringExtra("message") ?: ""

                if (message != "")
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

    }

    // 권한이 있는지 체크 하는 부분
    private fun checkPermission(): Boolean {
        var flag = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notPermittedPermissions.add(permission)
                flag = false
            }
        }
        return flag
    }


    // 카메라, 오디오 권한 요청 후 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = true
        when (requestCode) {
            PERMISSION -> {
                for (num in grantResults.indices) {
                    if (grantResults[num] != PackageManager.PERMISSION_GRANTED) {
                        permissionCheck = false
                        break
                    }
                }

                if (permissionCheck) {
                    val intent = Intent(this, CallActivity::class.java)
                    intent.putExtra("username", binding.username.text.toString())
                    launcher.launch(intent)
                } else {
                    Toast.makeText(this, "permission are not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
