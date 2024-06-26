package com.example.coroutineexample.coroutinebuilder

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.coroutineexample.R


class CoroutineBuilderActivity : AppCompatActivity() {

    private var fragmentManager: FragmentManager? = null
    private var transaction: FragmentTransaction? = null

    private val launchFragment: Fragment by lazy {
        LaunchFragment()
    }

    private val asyncFragment: Fragment by lazy {
        AsyncFragment()
    }

    private val runBlockingFragment: Fragment by lazy {
        RunBlockingFragment()
    }

    private val withContextFragment: Fragment by lazy {
        WithContextFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_builder)

        fragmentManager = supportFragmentManager
        transaction = fragmentManager?.beginTransaction()


        transaction?.replace(R.id.frameLayout, launchFragment)?.commitAllowingStateLoss();
    }

    fun clickHandler(view: View) {
        transaction = fragmentManager?.beginTransaction()
        when (view.id) {
            R.id.btn_fragmentA -> transaction?.replace(R.id.frameLayout, launchFragment)
                ?.commitAllowingStateLoss()

            R.id.btn_fragmentB -> transaction?.replace(R.id.frameLayout, asyncFragment)
                ?.commitAllowingStateLoss()

            R.id.btn_fragmentC -> transaction?.replace(R.id.frameLayout, runBlockingFragment)
                ?.commitAllowingStateLoss()

            R.id.btn_fragmentD -> transaction?.replace(R.id.frameLayout, withContextFragment)
                ?.commitAllowingStateLoss()
        }
    }
}