package w2022v9o12.simple.qr_with_zxing.qr_scan;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import w2022v9o12.simple.qr_with_zxing.databinding.ActivityScanQrBinding;

public class ScanQrActivity extends AppCompatActivity {

    private ActivityScanQrBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<Intent> zxingActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                if (intentResult.getContents() != null) {
                    binding.textview.setText(intentResult.getContents());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                zxingScan();  //스캔
            }
        });
    }

    private void zxingScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("QR 코드를 스캔해주세요.");
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false); //스캔 된 이미지 가져올 지
        integrator.setCaptureActivity(CustomScannerActivity.class);
        zxingActivityResultLauncher.launch(integrator.createScanIntent());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zxingScan();  // 카메라 권한이 허용된 경우 스캔 화면으로 이동
            } else {
                Toast.makeText(ScanQrActivity.this, "카메라 권한을 허가하셔야 합니다.", Toast.LENGTH_LONG).show();  // 카메라 권한이 거부된 경우
            }
        }
    }

}