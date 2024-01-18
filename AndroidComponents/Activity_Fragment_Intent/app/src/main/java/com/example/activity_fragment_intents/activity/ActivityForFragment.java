package com.example.activity_fragment_intents.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.activity_fragment_intents.R;
import com.example.activity_fragment_intents.fragment.FragmentA;
import com.example.activity_fragment_intents.fragment.FragmentB;

public class ActivityForFragment extends AppCompatActivity {
    private static final String TAG = "Fragment-LifeCycle1";

    private Button addBtn, replaceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "ActivityForFragment - onCreate");

        setContentView(R.layout.activity_for_fragment);


        addBtn = findViewById(R.id.addBtn);
        replaceBtn = findViewById(R.id.replaceBtn);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final Fragment[] tempFragment = {new FragmentB()};
        fragmentTransaction.add(R.id.fragment_container, tempFragment[0]);
        fragmentTransaction.commit();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManagerA = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionA = fragmentManagerA.beginTransaction();
                if (tempFragment[0] instanceof FragmentA) {
                    tempFragment[0] = new FragmentB();
                } else if (tempFragment[0] instanceof FragmentB) {
                    tempFragment[0] = new FragmentA();
                }
                fragmentTransactionA.replace(R.id.fragment_container, tempFragment[0]);
                fragmentTransactionA.commit();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "ActivityForFragment - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ActivityForFragment - onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "ActivityForFragment - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "ActivityForFragment - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ActivityForFragment - onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "ActivityForFragment - onRestart");
    }
}