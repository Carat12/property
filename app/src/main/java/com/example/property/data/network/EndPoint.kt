package com.example.property.data.network

class EndPoint {
    companion object{
        const val REGISTER_ENDPOINT = "auth/register"
        const val LOGIN_ENDPOINT = "auth/login"

        const val PROPERTY_IMAGE_ENDPOINT = "upload/property/picture"
        const val PROPERTY_ENDPOINT = "property"
        const val DELETE_PROPERTY_ENDPOINT = "property/{id}"

        const val TENANT_ENDPOINT = "tenant"
        const val DELETE_TENANT_ENDPOINT = "tenant/{id}"
    }
}