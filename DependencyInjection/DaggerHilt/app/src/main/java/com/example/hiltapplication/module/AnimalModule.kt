package com.example.hiltapplication.module

import com.example.hiltapplication.animal.Animal
import com.example.hiltapplication.animal.Cat
import com.example.hiltapplication.animal.Dog
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier

@Module // 모듈로 사용한다는 의미
@InstallIn(ActivityComponent::class) //어떤 범위에서 쓰이는가를 나타낸다. 범위에는 액티비티, 애플리케이션, 싱글턴 등 다양한 종류가 있다.
abstract class AnimalModule {

    @CatAnimalQualifier
    @Binds // 연결한다. 인터페이스의 구현을 제공한다.
    abstract fun CatImpl(cat: Cat): Animal

    @DogAnimalQualifier
    @Binds
    abstract fun DogImpl(cat: Dog): Animal

}

@Qualifier
annotation class CatAnimalQualifier

@Qualifier
annotation class DogAnimalQualifier