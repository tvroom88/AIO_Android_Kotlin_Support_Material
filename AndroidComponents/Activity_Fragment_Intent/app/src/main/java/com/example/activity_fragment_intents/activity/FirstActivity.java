package com.example.activity_fragment_intents.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.activity_fragment_intents.MainActivity;
import com.example.activity_fragment_intents.R;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "Activity-LifeCycle";
    Button moveFirstToSecondActivity, moveFromFirstToSecondActivityWithData;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        moveFirstToSecondActivity = findViewById(R.id.moveFromFirstToSecondActivity);
        moveFromFirstToSecondActivityWithData = findViewById(R.id.moveFromFirstToSecondActivityWithData);

        moveFirstToSecondActivity.setOnClickListener(v -> {
            Intent myIntent = new Intent(FirstActivity.this, SecondActivity.class);
            startActivity(myIntent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null){
                            Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        moveFromFirstToSecondActivityWithData.setOnClickListener(v -> {
            Intent myIntent = new Intent(FirstActivity.this, SecondActivity.class);
            myIntent.putExtra("data", "first-activity data");
            launcher.launch(myIntent);
        });

        Log.d(TAG, "FirstActivity - onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "FirstActivity - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "FirstActivity - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "FirstActivity - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "FirstActivity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FirstActivity - onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "FirstActivity - onRestart");
    }
}