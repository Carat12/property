package com.example.property.app

class Config {
    companion object {
        const val ACCOUNT_LANDLORD = "landlord"
        const val ACCOUNT_PROPERTY_MAN = "Property Man"
        const val ACCOUNT_TENANT = "tenant"
        const val ACCOUNT_VENDOR = "Vendor"
        val ACCOUNT_TYPE = arrayOf(ACCOUNT_LANDLORD, ACCOUNT_TENANT)

        const val BASE_URL = "https://apolis-property-management.herokuapp.com/api/"
        const val MESSAGAE_KEY = "message"

        const val FILE_NAME = "userinfo"
        const val USER_ID_KEY = "userId"
        const val USER_TYPE_KEY = "type"
        const val USER_NAME_KEY = "name"
        const val USER_EMAIL_KEY = "email"
    }
}