package com.example.pamfirebase

import android.app.Application
import com.example.pamfirebase.di.MahasiswaContainer

class MahasiswaApp: Application() {
    lateinit var container: MahasiswaContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer(this)
    }
}