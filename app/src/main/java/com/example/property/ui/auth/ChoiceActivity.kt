package com.example.property.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.property.R
import com.example.property.ui.auth.login.LoginActivity
import com.example.property.ui.auth.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_choice.*

class ChoiceActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        //init()
    }

/*private fun init() {
    b_choice_login.setOnClickListener(this)
    btn_choice_register.setOnClickListener(this)
}

override fun onClick(v: View?) {
    when(v){
        btn_choice_login -> startActivity(Intent(this, LoginActivity::class.java))
        btn_choice_register -> startActivity(Intent(this, RegisterActivity::class.java))
    }
}*/
}