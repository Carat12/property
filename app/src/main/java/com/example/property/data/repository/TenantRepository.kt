package com.example.property.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.property.app.Config
import com.example.property.data.models.Tenant
import com.example.property.data.models.GetTenantResponse
import com.example.property.data.models.PostOrDeleteTenantResponse
import com.example.property.data.network.MyApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TenantRepository {

    private val api = MyApi()

    companion object{
        private var instance: TenantRepository? = null

        fun getInstance(): TenantRepository{
            if(instance == null)
                instance = TenantRepository()
            return instance!!
        }
    }

    private var getTenantErrorMsg: String? = null
    fun getAllTenants(result: MutableLiveData<ArrayList<Tenant>>) {
        api.getTenants().enqueue(object : Callback<GetTenantResponse>{
            override fun onResponse(
                call: Call<GetTenantResponse>,
                response: Response<GetTenantResponse>
            ) {
                if(response.isSuccessful)
                    result.value = response.body()!!.data
                else{
                    getTenantErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<GetTenantResponse>, t: Throwable) {
                getTenantErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getGetTenantErrorMsg(): String{
        return getTenantErrorMsg!!
    }

    private var postTenantErrorMsg: String? = null
    fun addTenant(tenant: Tenant, result: MutableLiveData<String>){
        api.postTenant(tenant).enqueue(object : Callback<PostOrDeleteTenantResponse>{
            override fun onResponse(
                call: Call<PostOrDeleteTenantResponse>,
                response: Response<PostOrDeleteTenantResponse>
            ) {
                if (response.isSuccessful)
                    result.value = response.body()!!.message
                else{
                    postTenantErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<PostOrDeleteTenantResponse>, t: Throwable) {
                postTenantErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getPostTenantErrorMsg(): String{
        return postTenantErrorMsg!!
    }

    private var deleteTenantErrorMsg: String? = null
    fun deleteTenant(tenantId: String, result: MutableLiveData<String>) {
        api.deleteTenant(tenantId).enqueue(object : Callback<PostOrDeleteTenantResponse>{
            override fun onResponse(
                call: Call<PostOrDeleteTenantResponse>,
                response: Response<PostOrDeleteTenantResponse>
            ) {
                if(response.isSuccessful)
                    result.value = response.body()!!.message
                else{
                    deleteTenantErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<PostOrDeleteTenantResponse>, t: Throwable) {
                deleteTenantErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getDeleteTenantErrorMsg(): String{
        return deleteTenantErrorMsg!!
    }
}