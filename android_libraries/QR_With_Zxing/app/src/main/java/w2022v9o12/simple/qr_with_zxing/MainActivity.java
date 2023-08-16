package w2022v9o12.simple.qr_with_zxing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import w2022v9o12.simple.qr_with_zxing.databinding.ActivityMainBinding;
import w2022v9o12.simple.qr_with_zxing.databinding.ActivityScanQrBinding;
import w2022v9o12.simple.qr_with_zxing.qr_scan.ScanQrActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //QR Code 만들기
        binding.makeQRbtn.setOnClickListener(view -> goToOtherActivity(MakeQrActivity.class));

        //QR 스캔 화면으로 이동하기
        binding.scanQRbtn.setOnClickListener(view -> goToOtherActivity(ScanQrActivity.class));
    }

    //다른 Activity로 넘어가기
    private void goToOtherActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}