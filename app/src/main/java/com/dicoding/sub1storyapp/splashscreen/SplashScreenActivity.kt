package com.dicoding.sub1storyapp.splashscreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1storyapp.MainActivity
import com.dicoding.sub1storyapp.R
import com.dicoding.sub1storyapp.login.LoginActivity
import com.dicoding.sub1storyapp.utils.Session
import com.dicoding.sub1storyapp.utils.UiValue

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var pref: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        pref = Session(this)
        val isLogin = pref.isLogin
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    MainActivity.begin(this)
                    finish()
                }
                else -> {
                    LoginActivity.begin(this)
                    finish()
                }
            }
        }, UiValue.LOADING)
    }
}