package com.example.property.data.models

//specially for landlord to manage tenants
data class Tenant(
    var name: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var landlordId: String? = null,
    val _id: String? = null,
)