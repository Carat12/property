package com.example.property.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.property.app.Config
import com.example.property.data.models.LoginResponse
import com.example.property.data.models.RegisterResponse
import com.example.property.data.models.User
import com.example.property.data.network.MyApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository private constructor() {

    private val TABLE_NAME = "users"

    //network call api
    private val api = MyApi()
    private var signUpError: String? = null
    private var loginError: String? = null

    companion object {
        private var instance: AuthRepository? = null

        fun getInstance(): AuthRepository {
            if (instance == null)
                instance = AuthRepository()
            return instance!!
        }
    }

    fun registerUser(user: User, signUpResult: MutableLiveData<String>) {
        if (user.name.isNullOrEmpty() || user.email.isNullOrEmpty() || user.password.isNullOrEmpty()) {
            signUpError = "Please enter all required fields"
            signUpResult.value = null
        } else {
            api.registerUser(user).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful)
                        signUpResult.value = response.body()!!.message
                    else {
                        signUpError = JSONObject(
                            response.errorBody()!!.string()
                        ).getString(Config.MESSAGAE_KEY)
                        signUpResult.value = null
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    signUpError = t.message.toString()
                    signUpResult.value = null
                }
            })
        }
    }

    fun getSignUpError(): String {
        return signUpError!!
    }

    fun loginUser(user: User, loginResult: MutableLiveData<User>){
        if(user.email.isNullOrEmpty() || user.password.isNullOrEmpty()){
            loginError = "Please enter all required fields"
            loginResult.value = null
        }else{
            api.loginUser(user).enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful)
                        loginResult.value = response.body()!!.user
                    else{
                        loginError = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                        loginResult.value = null
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginError = t.message.toString()
                    loginResult.value = null
                }
            })
        }
    }

    fun getLoginError(): String {
        return loginError!!
    }
}