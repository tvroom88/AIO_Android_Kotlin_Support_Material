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

public class FragmentB extends Fragment {

    private static final String TAG = "Fragment-LifeCycle";

    public FragmentB() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FragmentB - onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "FragmentB - onCreateView");
        return inflater.inflate(R.layout.fragment_b, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "FragmentB - onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "FragmentB - onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "FragmentB - onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "FragmentB - onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "FragmentB - onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "FragmentB - onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "FragmentB - onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "FragmentB - onDetach");
    }
}