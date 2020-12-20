package com.example.property.ui.property

import android.location.Address
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.property.data.models.Property
import com.example.property.data.repository.PropertyRepository
import com.example.property.helper.SessionManager

class PropertyViewModel: ViewModel(){
    //data binding
    var property = Property()

    private val repository = PropertyRepository.getInstance()
    private var imagePath: String? = null

    //response
    var getPropertyResult: MutableLiveData<ArrayList<Property>> = MutableLiveData()
    var postPropertyImgResult: MutableLiveData<String> = MutableLiveData()
    var postPropertyResult: MutableLiveData<String> = MutableLiveData()

    fun getAllProperty() {
        repository.getAllProperty(getPropertyResult)
    }

    fun onAddPropertyClicked(view: View){
        Log.d("jun", "p: ${property.address},${property.city},${property.state},${property.country},${property.purchasePrice},$imagePath,${property.mortageInfo}")
        if(imagePath != null){
            repository.postPropertyImage(imagePath!!, postPropertyImgResult)
        }else{
            postNewProperty(null)
        }
    }

    fun postNewProperty(imagePath: String?) {
        property.image = imagePath
        property.userId = SessionManager.currentUser._id
        Log.d("jun", "p: ${property.userId}, ${property.image},${property.address}")
        repository.postNewProperty(property, postPropertyResult)
    }

    fun setImagePath(path: String) {
        imagePath = path
    }

    fun getPostImgErrorMsg(): String {
        return repository.getPostImgErrorMsg()
    }

    fun getPostPropertyErrorMsg(): String {
        return repository.getPostPropertyErrorMsg()
    }

    fun getGetPropertyErrorMsg(): String {
        return repository.getGetPropertyErrorMsg()
    }

    fun setPropertyAddress(address: Address?) {
        property.address = "${address?.featureName} ${address?.thoroughfare}"
        property.city = address?.locality
        property.state = address?.adminArea
        property.country = address?.countryName
        Log.d("jun", "yes: $property")
    }
}