package w2022v9o12.simple.practicecamerax;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.util.Consumer;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import w2022v9o12.simple.practicecamerax.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding viewBinding;

    private ImageCapture imageCapture;
    private ImageAnalysis imageAnalysis;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;

    private ExecutorService cameraExecutor;

    private static final String TAG = "CameraXApp";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = getRequiredPermissions();

    private int ratio = AspectRatio.RATIO_4_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera(this);
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener(v -> takePhoto());
        viewBinding.videoCaptureButton.setOnClickListener(v -> captureVideo());

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            return;
        }

        // Create time stamped name and MediaStore entry.
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        // Create output options object which contains file + metadata
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions
                .Builder(getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
                .build();

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onError(ImageCaptureException exc) {
                        Log.e(TAG, "Photo capture failed: " + exc.getMessage(), exc);
                    }

                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults output) {
                        String msg = "Photo capture succeeded: " + output.getSavedUri();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                    }
                }
        );
    }

    private void captureVideo() {

        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        recording = videoCapture.getOutput().prepareRecording(MainActivity.this, options).withAudioEnabled()
                .start(ContextCompat.getMainExecutor(MainActivity.this),
                        recordEvent -> {
                            if (recordEvent instanceof VideoRecordEvent.Start) {
                                viewBinding.videoCaptureButton.setText("Stop Capture");
                                viewBinding.videoCaptureButton.setEnabled(true);
                            } else if (recordEvent instanceof VideoRecordEvent.Finalize) {
                                VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) recordEvent;

                                if (!finalizeEvent.hasError()) {
                                    String msg = "Video capture succeeded: " +
                                            finalizeEvent.getOutputResults().getOutputUri();
                                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, msg);
                                } else {
                                    if (recording != null) {
                                        recording.close();
                                        recording = null;
                                    }
                                    Log.e(TAG, "Video capture ends with error: " + finalizeEvent.getError());
                                }
                                viewBinding.videoCaptureButton.setText("Start Capture");
                                viewBinding.videoCaptureButton.setEnabled(true);
                            }
                        });
    }

    private void startCamera(Context context) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewBinding.viewFinder.getSurfaceProvider());


                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

//                imageCapture = new ImageCapture.Builder()
//                        .setTargetAspectRatio(ratio)
//                        .build();
//

                imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, new LuminosityAnalyzer(new LumaListener() {
                    @Override
                    public void onLumaCalculated(double luma) {
                        Log.d(TAG, "Average luminosity: " + luma);
                    }
                }));


                // Select back camera as a default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Unbind use cases before rebinding
                cameraProvider.unbindAll();

                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);


            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(this);
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String[] getRequiredPermissions() {
        List<String> permissionsList = new ArrayList<>();
        permissionsList.add(Manifest.permission.CAMERA);
        permissionsList.add(Manifest.permission.RECORD_AUDIO);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        return permissionsList.toArray(new String[0]);
    }

    private interface LumaListener {
        void onLumaCalculated(double luma);
    }

    private class LuminosityAnalyzer implements ImageAnalysis.Analyzer {

        private LumaListener listener;

        public LuminosityAnalyzer(LumaListener listener) {
            this.listener = listener;
        }

        private byte[] toByteArray(ByteBuffer buffer) {
            buffer.rewind();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            return data;
        }

        private double average(int[] array) {
            double sum = 0;
            for (int value : array) {
                sum += value;
            }
            return sum / array.length;
        }

        @Override
        public void analyze(ImageProxy image) {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] data = toByteArray(buffer);
            int[] pixels = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                pixels[i] = data[i] & 0xFF;
            }
            double luma = average(pixels);

            listener.onLumaCalculated(luma);

            image.close();
        }
    }
}