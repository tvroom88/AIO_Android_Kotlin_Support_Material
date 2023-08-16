package w2022v9o12.simple.qr_with_zxing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import w2022v9o12.simple.qr_with_zxing.databinding.ActivityMakeQrBinding;

public class MakeQrActivity extends AppCompatActivity {

    private ActivityMakeQrBinding binding;
    private String url = "https://from-android-to-server.tistory.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.makeQRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeQR();
            }
        });
    }

    private void makeQR(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.qrCodeImg.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.d("here is error", e.toString());
        }
    }
}