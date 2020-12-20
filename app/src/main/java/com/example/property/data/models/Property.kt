package com.example.property.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.property.BR

class Property: BaseObservable(){
    var _id: String? = null
    var userId: String? = null
    //var address: String? = null
    //var city: String? = null
    //var state: String? = null
    //var country: String? = "United States"
    var image: String? = null
    var purchasePrice: String? = null
    var mortageInfo: Boolean? = null
    var propertyStatus: Boolean = true

    @get:Bindable
    var address: String? = null
    set(value) {
        field = value
        notifyPropertyChanged(BR.address)
    }

    @get:Bindable
    var city: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.city)
        }

    @get:Bindable
    var state: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.state)
        }

    @get:Bindable
    var country: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.country)
        }

    var addressLine: String? = "$city, $state, $country"

    fun setAddressLine(){
        addressLine = "$city, $state, $country"
    }
}