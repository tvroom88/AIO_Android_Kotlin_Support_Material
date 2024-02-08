package com.example.livedata

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NameViewModel

    lateinit var textView: TextView
    lateinit var editText: EditText
    private lateinit var changeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        editText = findViewById(R.id.editText)
        changeBtn = findViewById(R.id.changeBtn)

        viewModel = ViewModelProvider(this)[NameViewModel::class.java]

        viewModel.currentName.observe(this,  object : Observer<String> {
            override fun onChanged(value: String) {
               textView.text = value
            }
        })

        changeBtn.setOnClickListener {
            viewModel.changeName(editText.text.toString())
        }

    }
}