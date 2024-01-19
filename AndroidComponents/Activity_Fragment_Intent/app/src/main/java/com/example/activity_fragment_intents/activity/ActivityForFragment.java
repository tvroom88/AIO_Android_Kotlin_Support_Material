package com.example.activity_fragment_intents.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.activity_fragment_intents.R;
import com.example.activity_fragment_intents.fragment.FragmentA;
import com.example.activity_fragment_intents.fragment.FragmentB;
import com.example.activity_fragment_intents.fragment.FragmentC;

import java.util.ArrayList;
import java.util.List;

public class ActivityForFragment extends AppCompatActivity {
    private static final String TAG = "Fragment-LifeCycle1";
    private Fragment mFragment, mSecondFragment;
    private FrameLayout fragment_containerA;
    TextView textView;

    List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "ActivityForFragment - onCreate");

        setContentView(R.layout.activity_for_fragment);

        Button addBtn = findViewById(R.id.addBtn);
        Button removeBtn = findViewById(R.id.removeBtn);
        Button replaceBtn = findViewById(R.id.replaceBtn);
        textView = findViewById(R.id.count_fragment);

        fragmentList = new ArrayList<>();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mFragment = new FragmentB();
        fragmentTransaction.add(R.id.fragment_containerA, mFragment);
        fragmentTransaction.commit();

        addBtn.setOnClickListener(v -> {
            FragmentManager fragmentManagerA = getSupportFragmentManager();
            FragmentTransaction fragmentTransactionA = fragmentManagerA.beginTransaction();
            if (mFragment instanceof FragmentA) {
                mFragment = new FragmentB();
            } else if (mFragment instanceof FragmentB) {
                mFragment = new FragmentA();
            }
            fragmentTransactionA.add(R.id.fragment_containerA, mFragment);
            fragmentTransactionA.commit();

            textView.setText(String.valueOf(fragment_containerA.getChildCount()));
        });

        removeBtn.setOnClickListener(v -> {
            FragmentManager fragmentManagerA = getSupportFragmentManager();
            FragmentTransaction fragmentTransactionA = fragmentManagerA.beginTransaction();
//            fragmentTransactionA.remove(mFragment);
//            fragmentTransactionA.commit();
            for (Fragment fragment : fragmentManagerA.getFragments()) {
                fragmentTransactionA.remove(fragment);
            }
            fragmentTransactionA.commit();

            textView.setText(String.valueOf(fragment_containerA.getChildCount()));
        });

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mSecondFragment = new FragmentC();
        fragmentTransaction.add(R.id.fragment_containerB, mSecondFragment);
        fragmentTransaction.commit();

        replaceBtn.setOnClickListener(v -> {
            FragmentManager fragmentManagerA = getSupportFragmentManager();
            FragmentTransaction fragmentTransactionA = fragmentManagerA.beginTransaction();
            fragmentTransactionA.replace(R.id.fragment_containerA, mSecondFragment);
            fragmentTransactionA.commit();
        });

        fragment_containerA = findViewById(R.id.fragment_containerA);
        textView.setText(String.valueOf(fragment_containerA.getChildCount()));
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