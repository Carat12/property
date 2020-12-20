package com.example.property.ui.property.addproperty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.app.Config
import com.example.property.databinding.FragmentStepOneBinding
import com.example.property.ui.property.PropertyViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_step_one.view.*

class StepOneFragment : Fragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentStepOneBinding
    private lateinit var viewModel: PropertyViewModel
    private lateinit var fragmentView: View
    private val REQUEST_LOCATION = 444

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_one, container, false)
        fragmentView = mBinding.root
        init()

        return fragmentView
    }

    private fun init() {
        //view model
        registerViewModel()

        fragmentView.btn_next.setOnClickListener(this)
        fragmentView.btn_get_location.setOnClickListener(this)
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(activity!!).get(PropertyViewModel::class.java)
        mBinding.property = viewModel.property
    }

    override fun onClick(v: View?) {
        when(v){
            fragmentView.btn_get_location -> getCurrentLocation()
            fragmentView.btn_next -> {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_property, StepTwoFragment())
                    .addToBackStack("StepOne")
                    .commit()
            }
        }
    }

    private fun getCurrentLocation() {
        if(ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }else{
            val locationRequest = LocationRequest()
                .setInterval(10000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            LocationServices.getFusedLocationProviderClient(activity!!)
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult?) {
                        LocationServices.getFusedLocationProviderClient(activity!!)
                            .removeLocationUpdates(this)
                        if(result != null && result.locations.isNotEmpty()){
                            val location = result.locations[result.locations.size - 1]
                            Log.d("jun", "location: $location")
                            getAddressByLocation(location)
                        }
                    }
                }, Looper.myLooper())
        }
    }

    private fun getAddressByLocation(location: Location?) {
        val intent = Intent(activity, GetAddressIntentService::class.java)
        intent.putExtra(Config.LOCATION_KEY, location)
        intent.putExtra(Config.RECEIVER_KEY, AddressResultReceiver(Handler()))
        activity?.startService(intent)
    }

    inner class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler){
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            if(resultCode == Config.SUCCESS && resultData != null){
                val address = resultData.getParcelable<Address>(Config.RESULT_KEY)
                viewModel.setPropertyAddress(address)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("jun", "permission result")
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION)
            getCurrentLocation()
    }
}