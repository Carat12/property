package com.example.property.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.property.R
import com.example.property.ui.auth.ChoiceActivity

class SplashActivity : AppCompatActivity() {

    private val DELAY_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(
            {
            startActivity(Intent(this, ChoiceActivity::class.java))
        }, DELAY_TIME)
    }
}