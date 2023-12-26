package com.example.hiltapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hiltapplication.R
import com.example.hiltapplication.animal.Animal
import com.example.hiltapplication.animal.Cat
import com.example.hiltapplication.module.CatAnimalQualifier
import com.example.hiltapplication.module.DogAnimalQualifier
import com.example.hiltapplication.store.Store
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BlankFragment : Fragment() {

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        store.open()

        animal.bark()

        animalCat.bark()

        animalDog.bark()

        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

}