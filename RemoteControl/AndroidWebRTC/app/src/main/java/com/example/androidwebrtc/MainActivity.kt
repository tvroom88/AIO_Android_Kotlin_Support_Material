package com.example.androidwebrtc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidwebrtc.databinding.ActivityMainBinding

/**
 *  해야할일:
 *  (1)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 권한들중 허가 안된 권한들
    private val notPermittedPermissions: MutableList<String> = mutableListOf()

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
        binding.enterBtn.setOnClickListener {

            if (checkPermission()) {
                val intent = Intent(this, CallActivity::class.java)
                intent.putExtra("username", binding.username.text.toString())
                startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(this,
                    notPermittedPermissions.toTypedArray(), PERMISSION);
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
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "permission are not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}