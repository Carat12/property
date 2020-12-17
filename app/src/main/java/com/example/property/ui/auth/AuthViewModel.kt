package com.example.property.ui.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.property.data.models.User
import com.example.property.data.network.MyApi
import com.example.property.data.repository.AuthRepository

class AuthViewModel: ViewModel(){
    private lateinit var acctType: String
    var name: String? = null
    var email: String? = null
    var pw: String? = null
    var confirmPw: String? = null

    //repository
    private val repository = AuthRepository.getInstance()
    //response
    var signUpResult: MutableLiveData<String> = MutableLiveData()
    var loginResult: MutableLiveData<User> = MutableLiveData()

    companion object{
        lateinit var currentUser: User
    }

    fun setAcctType(acctType: String) {
        this.acctType = acctType
    }

    fun onSignUpClicked(view: View){
        Log.d("woozi", "$acctType, $email, $pw")
        val user = User(acctType, name, email, pw)
        repository.registerUser(user, signUpResult)
    }

    fun getSignUpError(): String {
        return repository.getSignUpError()
    }

    fun onLogInClicked(view: View){
        Log.d("woozi", "login: ${email}, ${pw}")
        val user = User(email = email, password = pw)
        repository.loginUser(user, loginResult)
    }

    fun getLoginError(): String {
        return repository.getLoginError()
    }

    fun setCurrentUser(t: User) {
        currentUser = t
        //Log.d("woozi", "${currentUser.type}. ${currentUser.email}, ${currentUser.pw}")
    }
}