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

public class FragmentA extends Fragment {

    private static final String TAG = "Fragment-LifeCycle1";

    public FragmentA() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "FragmentA - onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "FragmentA - onCreateView");
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FragmentA - onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "FragmentA - onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "FragmentA - onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "FragmentA - onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "FragmentA - onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "FragmentA - onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "FragmentA - onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "FragmentA - onDetach");
        super.onDetach();
    }
}