package com.example.property.data.network

import com.example.property.app.Config
import com.example.property.data.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface MyApi {

    @POST(EndPoint.REGISTER_ENDPOINT)
    fun registerUser(@Body user: User): Call<RegisterResponse>

    @POST(EndPoint.LOGIN_ENDPOINT)
    fun loginUser(@Body user: User): Call<LoginResponse>

    @GET(EndPoint.PROPERTY_ENDPOINT)
    fun getProperty(): Call<GetPropertyResponse>

    @POST(EndPoint.PROPERTY_ENDPOINT)
    fun postProperty(@Body property: Property): Call<PostOrDeletePropertyResponse>

    @Multipart
    @POST(EndPoint.PROPERTY_IMAGE_ENDPOINT)
    fun postPropertyImage(@Part image: MultipartBody.Part): Call<PropertyImgResponse>

    @DELETE(EndPoint.DELETE_PROPERTY_ENDPOINT)
    fun deleteProperty(@Path("id") id: String): Call<PostOrDeletePropertyResponse>

    @GET(EndPoint.TENANT_ENDPOINT)
    fun getTenants(): Call<GetTenantResponse>

    @POST(EndPoint.TENANT_ENDPOINT)
    fun postTenant(@Body tenant: Tenant): Call<PostOrDeleteTenantResponse>

    @DELETE(EndPoint.DELETE_TENANT_ENDPOINT)
    fun deleteTenant(@Path("id") id: String): Call<PostOrDeleteTenantResponse>


    companion object{
        operator fun invoke(): MyApi{
            return Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }
    }
}