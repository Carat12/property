package com.example.property.ui.document

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.app.Config
import com.example.property.databinding.ActivityAddDocBinding
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import com.example.property.ui.property.addproperty.UploadOptionsFragment
import kotlinx.android.synthetic.main.activity_add_doc.*
import kotlinx.android.synthetic.main.tool_bar.*

class AddDocActivity : AppCompatActivity(), UploadOptionsFragment.OnImageAddedListener, MyListener {

    private lateinit var mBinding: ActivityAddDocBinding
    private lateinit var viewModel: DocViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_doc)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Add Document"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        registerViewModel()

        btn_upload_image.setOnClickListener {
            val fragment = UploadOptionsFragment("document_")
            fragment.setImageAddedListener(this)
            fragment.show(supportFragmentManager, Config.TAG_SHOW_UPLOAD_OPTIONS_FRAGMENT)
        }
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(this).get(DocViewModel::class.java)
        mBinding.doc = viewModel.doc
        mBinding.viewModel = viewModel

        viewModel.addDocResult.observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getAddDocErrorMsg())
            }
        })
    }

    override fun onImageAddedFromCamera(img: Bitmap?, uri: Uri?, path: String) {
        img_view.setImageBitmap(img)
        viewModel.setDocUri(uri)
    }

    override fun onImageAddedFromGallery(uri: Uri?, path: String) {
        img_view.setImageURI(uri)
        viewModel.setDocUri(uri)
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        finish()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }
}