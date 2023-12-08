package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.interfaces.NewMsgInterface
import com.example.myapplication.models.DataModel

class MainActivity : AppCompatActivity(), NewMsgInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var socketRepository: SocketRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        socketRepository = SocketRepository(this) //소켓 사용

        socketRepository.initSocket(binding.editTextText.text.toString())

        binding.button.setOnClickListener {
            Log.d("MyName", "My name is " + binding.editTextText.text.toString())
        }

    }

    override fun onNewMsg(message: DataModel) {
        TODO("Not yet implemented")
    }
}