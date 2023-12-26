package com.example.hiltapplication.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides // 다른 곳에서 사용될 객체를 만들어 준다는 뜻이다
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            interceptors().add(httpLoggingInterceptor)
        }.build()


    @Provides
    fun provideRequest(): Request {
        return Request.Builder()
            .url("https://www.google.com")
            .header("User-Agent", "OkHttp Example")
            .build()
    }


//    @Provides
//    @Singleton
//    fun provideApiService(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(MoshiConvertFactory.create())
//            .client(okHttpClient)
//            .baseUrl(BASE_URL)
//            .build()
//    }
//
//
//    @Singleton
//    @Provides
//    fun provideApiService(retrofit: Retrofit): BookSearchApi {
//        return retrofit.create(BookSearchApi::class.java)
//    }
}