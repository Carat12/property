package com.example.property.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.property.app.Config
import com.example.property.data.models.GetPropertyResponse
import com.example.property.data.models.PostPropertyResponse
import com.example.property.data.models.Property
import com.example.property.data.models.PropertyImgResponse
import com.example.property.data.network.MyApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class PropertyRepository {

    private val api = MyApi()
    private var getPropertyErrorMsg: String? = null
    private var postImgErrorMsg: String? = null
    private var postPropertyErrorMsg: String? = null

    companion object{
        private var instance: PropertyRepository? = null

        fun getInstance(): PropertyRepository{
            if (instance == null)
                instance = PropertyRepository()
            return instance!!
        }
    }

    fun postNewProperty(property: Property, result: MutableLiveData<String>){
        api.postProperty(property).enqueue(object : Callback<PostPropertyResponse>{
            override fun onResponse(
                call: Call<PostPropertyResponse>,
                response: Response<PostPropertyResponse>
            ) {
                if(response.isSuccessful)
                    result.value = response.body()!!.message
                else{
                    postPropertyErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    result.value = null
                }
            }

            override fun onFailure(call: Call<PostPropertyResponse>, t: Throwable) {
                postPropertyErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun postPropertyImage(path: String, result: MutableLiveData<String>){
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse("*/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.name,requestFile)
        api.postPropertyImage(body).enqueue(object : Callback<PropertyImgResponse>{
            override fun onResponse(
                call: Call<PropertyImgResponse>,
                response: Response<PropertyImgResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("jun", "success: ${response.body()}")
                    result.value = response.body()!!.data.location
                }
                else {
                    postImgErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    Log.d("jun", "fail: $postImgErrorMsg")
                    result.value = null
                }
            }
            override fun onFailure(call: Call<PropertyImgResponse>, t: Throwable) {
                postImgErrorMsg = t.message.toString()
                Log.d("jun", "fail: $postImgErrorMsg")
                result.value = null
            }
        })
    }

    fun getPostImgErrorMsg(): String {
        return postImgErrorMsg!!
    }

    fun getPostPropertyErrorMsg(): String {
        return postPropertyErrorMsg!!
    }

    fun getAllProperty(result: MutableLiveData<ArrayList<Property>>) {
        api.getProperty().enqueue(object : Callback<GetPropertyResponse>{
            override fun onResponse(
                call: Call<GetPropertyResponse>,
                response: Response<GetPropertyResponse>
            ) {
                if(response.isSuccessful)
                    result.value = response.body()!!.data
                else{
                    getPropertyErrorMsg = JSONObject(response.errorBody()!!.string()).getString(Config.MESSAGAE_KEY)
                    result.value = null
                }
            }
            override fun onFailure(call: Call<GetPropertyResponse>, t: Throwable) {
                getPropertyErrorMsg = t.message.toString()
                result.value = null
            }
        })
    }

    fun getGetPropertyErrorMsg(): String {
        return getPropertyErrorMsg!!
    }
}