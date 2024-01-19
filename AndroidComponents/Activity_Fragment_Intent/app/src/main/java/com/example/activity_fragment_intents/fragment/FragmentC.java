package com.example.activity_fragment_intents.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activity_fragment_intents.R;

public class FragmentC extends Fragment {
    private static final String TAG = "Fragment-LifeCycle";

    public FragmentC() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FragmentC - onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "FragmentC - onCreateView");
        return inflater.inflate(R.layout.fragment_c, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "FragmentC - onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "FragmentC - onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FragmentC - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "FragmentC - onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "FragmentC - onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "FragmentC - onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FragmentC - onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "FragmentC - onDetach");
    }
}