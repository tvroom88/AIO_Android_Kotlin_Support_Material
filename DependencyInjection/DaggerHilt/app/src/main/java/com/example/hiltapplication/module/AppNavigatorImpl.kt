package com.example.hiltapplication.module

import androidx.fragment.app.FragmentActivity
import com.example.hiltapplication.R
import com.example.hiltapplication.fragments.BlankFragment
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val activity: FragmentActivity
) : AppNavigator {

    override fun navigateTo(screen: Screens) {
        val fragment = when (screen) {
            Screens.FIRST -> BlankFragment()
        }

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(fragment::class.java.canonicalName)
            .commit()
    }
}
