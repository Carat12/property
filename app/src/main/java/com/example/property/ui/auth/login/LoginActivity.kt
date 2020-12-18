package com.example.property.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.data.models.User
import com.example.property.databinding.ActivityLoginBinding
import com.example.property.helper.SessionManager
import com.example.property.helper.toast
import com.example.property.ui.HomeActivity
import com.example.property.ui.MyListener
import com.example.property.ui.auth.AuthViewModel
import com.example.property.ui.auth.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), MyListener {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        init()
    }

    private fun init() {
        //register view model
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        mBinding.viewModel = viewModel
        viewModel.loginResult.observe(this, object : Observer<User>{
            override fun onChanged(t: User?) {
                if(t != null) {
                    onSuccess("Login succeeded!")
                    SessionManager.getInstance(this@LoginActivity).saveCurrentUser(t)
                    //viewModel.setCurrentUser(t)
                }else
                    onFailure(viewModel.getLoginError())
            }
        })

        //redirect to signup
        btn_to_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}