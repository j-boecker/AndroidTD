package com.example.towerdefense_v1

import android.app.Application
import android.content.Context

public class ApplicationContextProvider : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: ApplicationContextProvider? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = ApplicationContextProvider.applicationContext()
    }
}