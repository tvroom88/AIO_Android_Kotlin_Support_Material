package com.example.activity_fragment_intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.activity_fragment_intents.activity.ActivityForFragment;
import com.example.activity_fragment_intents.activity.FirstActivity;

public class MainActivity extends AppCompatActivity {

    Button moveFirstActivity, moveToFragmentExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moveFirstActivity = findViewById(R.id.moveToFirstActivity);
        moveToFragmentExample = findViewById(R.id.moveToFragmentExample);

        moveFirstActivity.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(myIntent);
        });

        moveToFragmentExample.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ActivityForFragment.class);
            startActivity(myIntent);
        });

    }
}