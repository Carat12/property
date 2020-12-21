package com.example.property.data.models

//for landlord or tenant to register and login
data class User(
    val type: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val landlordEmail: String? = null,
    val _id: String? = null,
    val createdAt: String? = null
){
    companion object{
        const val KEY_USER = "User"
    }
}
