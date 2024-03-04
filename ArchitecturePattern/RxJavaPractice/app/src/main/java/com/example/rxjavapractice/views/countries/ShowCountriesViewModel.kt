package com.example.rxjavapractice.views.countries


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavapractice.dataModel.CountryModel
import com.example.rxjavapractice.repository.NetworkRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class ShowCountriesViewModel : ViewModel() {

    val networkRepository = NetworkRepository()

    // 사용자에게 보여줄 국가 데이터
    var countries = MutableLiveData<List<CountryModel>>()

    // 국가 데이터를 가져오는 것에 성공했는지를 알려주는 데이터
    var countryLoadError = MutableLiveData<Boolean>()

    // 로딩 중인지를 나타내는 데이터
    var loading = MutableLiveData<Boolean>()

    private val disposable: CompositeDisposable = CompositeDisposable()

    suspend fun refresh() {
        fetchCountries()
    }
    private suspend fun fetchCountries() {
        // 서버로부터 데이터를 받아오는 동안에 로딩 스피너를 보여주기 위함
        loading.value = true
        disposable.add(
            networkRepository.getCountries() // Single<List<CountryModel>>를 반환한다.
                .subscribeOn(Schedulers.newThread()) // 새로운 스레드에서 통신한다.
                .observeOn(AndroidSchedulers.mainThread()) // 응답 값을 가지고 ui update를 하기 위해 필요함, 메인스레드와 소통하기 위
                .subscribeWith(object : DisposableSingleObserver<List<CountryModel>>() {
                    override fun onSuccess(countryModels: List<CountryModel>) {
                        countries.value = countryModels
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(@NonNull e: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()

        // 앱이 통신 중에 프로세스가 종료될 경우(앱이 destory됨)
        // 메모리 손실을 최소화 하기 위해 백그라운드 스레드에서 통신 작업을 중단한다.
        disposable.clear()
    }
}
