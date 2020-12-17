package com.example.property.ui.property

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.property.ui.auth.MyListener
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.tool_bar.*

class AddPropertyActivity : AppCompatActivity(), View.OnClickListener, MyListener {

    private lateinit var mBinding: ActivityAddPropertyBinding
    private lateinit var viewModel: PropertyViewModel
    private val PICK_FROM_GALLERY = 222

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
        viewModel = ViewModelProvider(this).get(PropertyViewModel::class.java)
        mBinding.property = viewModel.property
        mBinding.viewModel = viewModel

        //observe posting image result
        viewModel.postPropertyImgResult.observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null) {
                    Log.d("jun", "imgpath: $t")
                    viewModel.postNewProperty(t)
                }
                else
                    onFailure(viewModel.getPostImgErrorMsg())
            }
        })

        //observe posting property result
        viewModel.postPropertyResult.observe(this, object: Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getPostPropertyErrorMsg())
            }
        })

        //buttons
        btn_camera.setOnClickListener(this)
        btn_gallery.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v){
            btn_gallery -> openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data
                    img_view.setImageURI(uri)
                    Log.d("jun", "uri: $uri")

                    val path = getPathFromUri(uri!!)
                    Log.d("jun", "path: $path")
                    viewModel.setImagePath(path)//postPropertyImage(path)
                }
            }
        }
    }

    fun getPathFromUri(uri: Uri): String{
        var cursor: Cursor? = null;
        try {
            val proj =  arrayOf(MediaStore.Images.Media.DATA)
            cursor = getContentResolver().query(uri,  proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: Exception) {
            Log.d("jun", "getRealPathFromURI Exception : " + e.toString())
            return ""
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