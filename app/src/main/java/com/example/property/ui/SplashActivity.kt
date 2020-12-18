package com.example.property.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.property.R
import com.example.property.helper.SessionManager
import com.example.property.ui.auth.ChoiceActivity

class SplashActivity : AppCompatActivity() {

    private val DELAY_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(
            {
                checkLoggedIn()
        }, DELAY_TIME)
    }

    private fun checkLoggedIn() {
        if(SessionManager.getInstance(this).checkLoggedIn())
            startActivity(Intent(this, HomeActivity::class.java))
        else
            startActivity(Intent(this, ChoiceActivity::class.java))
    }
}