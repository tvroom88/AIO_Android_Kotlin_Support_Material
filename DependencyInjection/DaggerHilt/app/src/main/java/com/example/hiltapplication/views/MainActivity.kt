package com.example.hiltapplication.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hiltapplication.R
import com.example.hiltapplication.animal.Animal
import com.example.hiltapplication.animal.Cat
import com.example.hiltapplication.module.AppNavigator
import com.example.hiltapplication.module.CatAnimalQualifier
import com.example.hiltapplication.module.DogAnimalQualifier
import com.example.hiltapplication.module.Screens
import com.example.hiltapplication.store.Store
import com.example.hiltapplication.viewModels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 의존성 주입이 없었다면
    // lateinit var store: Store

    @Inject
    lateinit var store: Store

    @Inject
    lateinit var animal: Cat

    @CatAnimalQualifier
    @Inject
    lateinit var animalCat: Animal // interface를 2개가 상속 받는데 그냥 사용하면 어느게 받는건지 모르기 때문에 module을 추가 해야 한다.

    @DogAnimalQualifier
    @Inject
    lateinit var animalDog: Animal

    // okhttp3 라이브러리에 의존성 주입을 넣기 위한 부분
    @Inject
    lateinit var httpClient: OkHttpClient

    @Inject
    lateinit var request: Request

    @Inject
    lateinit var navigator: AppNavigator


//    val viewModel: MyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//의존성 주입이 없을때는 이런 방식으로
//        store = Store()
//        store.open()

        //의존성 주입이 있을때
//        store.open()
//
//        animal.bark()
//
//        // Animal 타입을 해준 Cat
//        animalCat.bark()
//
//        // Animal 타입을 해준 Dog
//        animalDog.bark()
//
//        httpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.i("httpClient", "Network call error - ${call}, err msg - ${e.message}")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Log.i("httpClient", "Network call - ${response.body}")
//            }
//        })

        navigator.navigateTo(Screens.FIRST)
    }
}