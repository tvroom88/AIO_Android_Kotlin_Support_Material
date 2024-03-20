package com.example.rxjavapractice.views.countries


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rxjavapractice.R
import com.example.rxjavapractice.views.adapter.CountryListAdapter

class ShowCountriesActivity : AppCompatActivity() {
    var countriesList: RecyclerView? = null
    var listError: TextView? = null
    var loadingView: ProgressBar? = null
    private var refreshLayout: SwipeRefreshLayout? = null

    private val viewModel: ShowCountriesViewModel by viewModels()

    private val adapter: CountryListAdapter = CountryListAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_countries)

        countriesList = findViewById(R.id.countriesList)
        listError = findViewById(R.id.list_error)
        refreshLayout = findViewById(R.id.swipeRefreshLayout)
        loadingView = findViewById(R.id.loading_view)

        // 국가 데이터를 가져온다.
        viewModel.refresh()

        // 리사이클러뷰에 어뎁터를 설정한다.
        countriesList?.layoutManager = LinearLayoutManager(this)
        countriesList?.adapter = adapter

        refreshLayout?.setOnRefreshListener {

            // 리프레이 될 때마다 새로운 데이터를 가져온다.
            viewModel.refresh()
            refreshLayout?.isRefreshing = false
        }


        observerViewModel()
    }


    private fun observerViewModel() {
        /**
         * 뷰(메인 화면)에 라이브 데이터를 붙인다.
         * 메인 화면에서 관찰할 데이터를 설정한다.
         * 라이브 데이터가 변경됐을 때 변경된 데이터를 가지고 UI를 변경한다.
         */
        viewModel.countries.observe(this) { countryModels ->

            // 데이터 값이 변할 때마다 호출된다.
            if (countryModels != null) {
                countriesList!!.visibility = View.VISIBLE
                // 어뎁터가 리스트를 수정한다.
                adapter.updateCountries(countryModels)
            }
        }
        viewModel.countryLoadError.observe(this) { isError ->
            // 에러 메세지를 보여준다.
            if (isError != null) {
                listError!!.visibility = if (isError) View.VISIBLE else View.GONE
            }
        }
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading != null) {
                // 로딩 중이라는 것을 보여준다.
                loadingView!!.visibility = if (isLoading) View.VISIBLE else View.GONE
                // 로딩중일 때 에러 메세지, 국가 리스트는 안 보여준다.
                if (isLoading) {
                    listError!!.visibility = View.GONE
                    countriesList!!.visibility = View.GONE
                }
            }
        }
    }
}
