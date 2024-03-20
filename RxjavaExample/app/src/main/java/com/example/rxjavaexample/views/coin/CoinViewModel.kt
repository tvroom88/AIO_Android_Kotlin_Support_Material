package com.example.rxjavaexample.views.coin

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavaexample.network.networkModel.CurrentPriceList
import com.example.rxjavaexample.repository.NetworkRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()

    // 사용자에게 보여줄 국가 데이터
    var countries = MutableLiveData<CurrentPriceList>()


    private val disposable: CompositeDisposable = CompositeDisposable()

    private fun getCoinsList() {

        val coinList: Single<CurrentPriceList> = networkRepository.getCurrentCoinList()

        val coins = coinList // Single<List<CountryModel>>를 반환한다.
            .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
            .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
            .subscribeWith(object : DisposableSingleObserver<CurrentPriceList>() {
                override fun onSuccess(currentPriceList: CurrentPriceList) {
                    countries.value = currentPriceList

                }
                override fun onError(@NonNull e: Throwable) {
                    e.printStackTrace()
                }
            })



        if (coins != null) {
            disposable.add(coins)
        }
    }
}