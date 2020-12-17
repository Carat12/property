package com.example.property.data.models

data class Property(
    var _id: String? = null,
    var userId: String? = null,
    var address: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var image: String? = null,
    var purchasePrice: String? = null,
    var mortageInfo: Boolean? = null,
    var propertyStatus: Boolean? = null
){
    var addressLine = "$city, $state, $country"
}