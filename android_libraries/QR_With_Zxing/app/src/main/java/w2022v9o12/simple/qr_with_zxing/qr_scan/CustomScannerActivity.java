package w2022v9o12.simple.qr_with_zxing.qr_scan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.lang.reflect.Field;

import w2022v9o12.simple.qr_with_zxing.databinding.ActivityCustomScannerBinding;

public class CustomScannerActivity extends AppCompatActivity {

    private ActivityCustomScannerBinding binding;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        capture = new CaptureManager(this, binding.zxingBarcodeScanner);
        capture.initializeFromIntent(getIntent(), savedInstanceState);

        capture.decode();

        //---- 레이저 라인 없애는 코드 ----
        ViewfinderView viewfinderview = binding.zxingBarcodeScanner.getViewFinder();
        Field scannerAlphaField = null;
        try {
            scannerAlphaField = viewfinderview.getClass().getDeclaredField("SCANNER_ALPHA");
            scannerAlphaField.setAccessible(true);
            scannerAlphaField.set(viewfinderview, new int[1]);
        } catch (Exception e) {
            Log.e("eTag", "e : " + e.getMessage());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

}