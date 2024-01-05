package com.example.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            // **중요** binding에 LifeCycleOwner을 지정해줘야 LiveData가 실시간으로 변화
            lifecycleOwner = this@MainActivity

            // xml 파일에 선언한 viewModel
            viewModel = mainViewModel
        }
    }

    fun getRandomCharacter() {
        mainViewModel.getRandomCharacter()
    }
}