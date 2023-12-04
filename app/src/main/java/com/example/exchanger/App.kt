package com.example.exchanger

import android.app.Application
import com.example.exchanger.di.coroutineModule
import com.example.exchanger.di.localStorageModule
import com.example.exchanger.di.mainModule
import com.example.exchanger.di.networkModule
import com.example.exchanger.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDi()
    }

    private fun setupDi() {
        startKoin {
            androidContext(this@App)
            modules(
                mainModule,
                localStorageModule,
                networkModule,
                coroutineModule,
                useCaseModule,
            )
        }
    }

}