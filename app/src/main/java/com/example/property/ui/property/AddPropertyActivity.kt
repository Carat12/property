package com.example.property.ui.property

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.databinding.ActivityAddPropertyBinding
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.io.File

class AddPropertyActivity : AppCompatActivity(), View.OnClickListener, MyListener {

    private lateinit var mBinding: ActivityAddPropertyBinding
    private lateinit var viewModel: PropertyViewModel
    private val IMAGE_CAPTURE = 111
    private val PICK_FROM_GALLERY = 222
    private val OPEN_SETTINGS = 333

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Add Property"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //view model
        registerViewModel()

        //buttons
        btn_camera.setOnClickListener(this)
        btn_gallery.setOnClickListener(this)
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(this).get(PropertyViewModel::class.java)
        mBinding.property = viewModel.property
        mBinding.viewModel = viewModel
        //observe posting image result
        viewModel.postPropertyImgResult.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (t != null) {
                    Log.d("jun", "imgpath: $t")
                    viewModel.postNewProperty(t)
                } else
                    onFailure(viewModel.getPostImgErrorMsg())
            }
        })

        //observe posting property result
        viewModel.postPropertyResult.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getPostPropertyErrorMsg())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onClick(v: View?) {
        checkPermissions(v)
    }

    private fun checkPermissions(v: View?) {
        Dexter.withContext(this)
            .withPermissions(
                arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        when (v) {
                            btn_gallery -> openGallery()
                            btn_camera -> openCamera()
                        }
                    }
                    if(report.isAnyPermissionPermanentlyDenied){
                        showDialog()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Permanently Denied")
            .setMessage("If you want to upload images, please go to Settings to grant all required permissions")
            .setPositiveButton("Go to Settings", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    openSettings()
                }
            })
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                }
            })
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)//, OPEN_SETTINGS)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data
                    img_view.setImageURI(uri)
                    Log.d("jun", "uri: $uri")

                    val path = getPathFromUri(uri!!)
                    Log.d("jun", "path: $path")
                    viewModel.setImagePath(path)
                }
            }
            IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val img = data?.extras?.get("data") as? Bitmap
                    img_view.setImageBitmap(img)
                    Log.d("jun", "camera img: $img")

                    val uri = saveToGallery(img)
                    viewModel.setImagePath(getPathFromUri(uri))
                }
            }
        }
    }

    private fun saveToGallery(img: Bitmap?): Uri? {
        var uri: Uri? = null
        if(img != null){
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "property_" + System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if(uri != null){
                val outputStreem = contentResolver.openOutputStream(uri)
                try{
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outputStreem)
                }catch (e: Exception){
                    Log.d("jun",e.message.toString())
                }finally {
                    outputStreem?.close()
                }
            }
        }
        return uri
    }

    fun getPathFromUri(uri: Uri?): String {
        if(uri == null)
            return ""
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = getContentResolver().query(uri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            ""
        } finally {
            cursor?.close()
        }
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        finish()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}