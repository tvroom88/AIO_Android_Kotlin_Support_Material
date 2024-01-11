package com.example.activity_fragment_intents.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.activity_fragment_intents.R;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Activity-LifeCycle";
    Button goBackToFirstActivity, goBackToFirstActivityWithData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        goBackToFirstActivity = findViewById(R.id.goBackToFirstActivity);
        goBackToFirstActivityWithData =findViewById(R.id.goBackToFirstActivityWithData);



        String data = "";
        Intent intent = getIntent();
        if(!TextUtils.isEmpty(intent.getStringExtra("data"))){
            data = intent.getStringExtra("data");
        }else{
            data = "data is null";
        }
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

        Log.d(TAG, "SecondActivity - onCreate");

        String finalData = data;
        goBackToFirstActivity.setOnClickListener(v -> {
            finish();
        });

        goBackToFirstActivityWithData.setOnClickListener(v -> {
            Intent myIntent = new Intent();
            intent.putExtra("data", finalData);
            setResult(0, intent);
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "SecondActivity - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "SecondActivity - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "SecondActivity - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "SecondActivity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SecondActivity - onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "SecondActivity - onRestart");
    }
}