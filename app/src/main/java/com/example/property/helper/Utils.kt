package com.example.property.helper

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String){
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}