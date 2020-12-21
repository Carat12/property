package com.example.property.ui.tenant

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.property.data.models.Tenant
import com.example.property.data.repository.TenantRepository
import com.example.property.helper.SessionManager

class TenantViewModel: ViewModel() {
    //data binding in AddTenantActivity
    var tenant = Tenant(landlordId = SessionManager.currentUser._id)
    //repository
    private val repository = TenantRepository.getInstance()
    //response
    var getTenantResult: MutableLiveData<ArrayList<Tenant>> = MutableLiveData()
    var postTenantResult: MutableLiveData<String> = MutableLiveData()
    var deleteTenantResult: MutableLiveData<String> = MutableLiveData()

    fun getAllTenants(){
        repository.getAllTenants(getTenantResult)
    }

    fun getGetTenantErrorMsg(): String {
        return repository.getGetTenantErrorMsg()
    }

    fun onAddTenantClicked(view: View){
        repository.addTenant(tenant, postTenantResult)
    }

    fun getPostTenantErrorMsg(): String{
        return repository.getPostTenantErrorMsg()
    }

    fun deleteTenant(tenantId: String){
        repository.deleteTenant(tenantId, deleteTenantResult)
    }

    fun getDeleteTenantErrorMsg(): String{
        return repository.getDeleteTenantErrorMsg()
    }
}