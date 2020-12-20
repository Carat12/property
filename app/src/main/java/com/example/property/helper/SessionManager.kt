package com.example.property.helper

import android.content.Context
import com.example.property.app.Config
import com.example.property.data.models.User

class SessionManager(context: Context) {

    private val sp = context.getSharedPreferences(Config.FILE_NAME, Context.MODE_PRIVATE)
    private val editor = sp.edit()

    companion object{
        private var instance: SessionManager? = null
        lateinit var currentUser: User

        fun getInstance(context: Context): SessionManager{
            if(instance == null)
                instance = SessionManager(context)
            return instance!!
        }

        fun isTenant(): Boolean{
            return currentUser.type!! == Config.ACCOUNT_TENANT
        }
    }

    fun saveCurrentUser(user: User){
        editor.putString(Config.USER_ID_KEY, user._id)
        editor.putString(Config.USER_TYPE_KEY, user.type)
        editor.putString(Config.USER_NAME_KEY, user.name)
        editor.putString(Config.USER_EMAIL_KEY, user.email)
        editor.apply()
        currentUser = user
    }

    fun checkLoggedIn(): Boolean{
        val id = sp.getString(Config.USER_ID_KEY, null)
        val type = sp.getString(Config.USER_TYPE_KEY, null)
        val name = sp.getString(Config.USER_NAME_KEY, null)
        val email = sp.getString(Config.USER_EMAIL_KEY, null)
        currentUser = User(type, name, email, _id = id)
        return id != null
    }

    fun logout() {
        editor.clear().commit()
    }
}