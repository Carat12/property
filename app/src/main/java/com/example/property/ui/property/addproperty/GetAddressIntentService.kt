package com.example.property.ui.property.addproperty

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.example.property.app.Config

class GetAddressIntentService : IntentService("GetAddressIntentService") {

    private var receiver: ResultReceiver? = null

    override fun onHandleIntent(intent: Intent?) {
        if(intent != null){
            val location = intent.getParcelableExtra<Location>(Config.LOCATION_KEY)
            receiver = intent.getParcelableExtra(Config.RECEIVER_KEY)
            if (location != null){
                var addressList: List<Address>? = null
                try{
                    addressList = Geocoder(this).getFromLocation(location.latitude, location.longitude, 1)
                }catch (e: Exception){
                    Log.d("jun", e.message.toString())
                    setResultReceiver(Config.FAILURE, null)
                }
                if(!addressList.isNullOrEmpty()){
                    val address = addressList[0]
                    setResultReceiver(Config.SUCCESS, address)
                }
            }else
                setResultReceiver(Config.FAILURE, null)
        }
    }

    private fun setResultReceiver(resultCode: Int, address: Address?){
        val bundle = Bundle()
        bundle.putParcelable(Config.RESULT_KEY, address)
        receiver?.send(resultCode, bundle)
    }
}



/*
Log.d("jun", "address: $address")
Log.d("jun", "country: ${address.countryName}")
Log.d("jun", "thoroughfare: ${address.thoroughfare}")
Log.d("jun", "street: ${address.featureName}")
Log.d("jun", "Locality: ${address.locality}")
Log.d("jun", "state: ${address.adminArea}")*/
