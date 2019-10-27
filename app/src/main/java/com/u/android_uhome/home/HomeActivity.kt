package com.u.android_uhome.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.u.android_uhome.R

class HomeActivity : AppCompatActivity() {

    lateinit var interactor: HomeInteractor
    lateinit var router: HomeRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        HomeConfigure.configure(this)
    }
}
