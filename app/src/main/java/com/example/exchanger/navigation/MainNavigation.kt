package com.example.exchanger.navigation

import androidx.navigation.NavController

class MainNavigation(private val navController: NavController) {

    fun back() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStack(resId: Int, include: Boolean) {
        navController.popBackStack(resId, include)
    }
}

//Зробити сторедж для даних по типу як Бенні тільки на префах. А може тупо на префах.