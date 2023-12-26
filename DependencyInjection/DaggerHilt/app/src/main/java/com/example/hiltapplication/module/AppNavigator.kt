package com.example.hiltapplication.module

enum class Screens {
    FIRST
}
interface AppNavigator {
    fun navigateTo(screen: Screens)
}