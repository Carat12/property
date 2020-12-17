package com.example.property.data.models

data class User(
    val type: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val _id: String? = null,
    val createdAt: String? = null
){
    companion object{
        const val KEY_USER = "User"
    }
}