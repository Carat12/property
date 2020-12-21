package com.example.property.data.models

data class RegisterResponse(
    val data: User,
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class PropertyImgResponse(
    val data: Image,
    val error: Boolean,
    val message: String
)

data class Image(
    val acl: String,
    val bucket: String,
    val contentDisposition: Any,
    val contentType: String,
    val encoding: String,
    val etag: String,
    val fieldname: String,
    val key: String,
    val location: String,
    val metadata: Metadata,
    val mimetype: String,
    val originalname: String,
    val serverSideEncryption: Any,
    val size: Int,
    val storageClass: String
)

data class Metadata(
    val fieldName: String
)

data class PostOrDeletePropertyResponse(
    val data: Property,
    val error: Boolean,
    val message: String
)

data class GetPropertyResponse(
    val data: ArrayList<Property>,
    val error: Boolean
)

data class GetTenantResponse(
    val count: Int,
    val data: ArrayList<Tenant>,
    val error: Boolean
)

data class PostOrDeleteTenantResponse(
    val data: Tenant,
    val error: Boolean,
    val message: String
)