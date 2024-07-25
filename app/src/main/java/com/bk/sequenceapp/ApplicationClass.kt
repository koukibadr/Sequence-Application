package com.bk.sequenceapp

import android.app.Application
import com.bk.sequenceapp.di.AppComponent
import com.bk.sequenceapp.di.DaggerAppComponent

class ApplicationClass: Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }

}