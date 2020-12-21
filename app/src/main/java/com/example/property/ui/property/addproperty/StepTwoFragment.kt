package com.example.property.ui.property.addproperty

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.app.Config
import com.example.property.databinding.FragmentStepTwoBinding
import com.example.property.ui.property.PropertyViewModel
import kotlinx.android.synthetic.main.fragment_step_two.view.*
import kotlinx.android.synthetic.main.fragment_step_two.view.btn_next

class StepTwoFragment : Fragment(), View.OnClickListener,
    UploadOptionsFragment.OnImageAddedListener {

    private lateinit var mBinding: FragmentStepTwoBinding
    private lateinit var fragmentView: View
    private lateinit var viewModel: PropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_two, container, false)
        fragmentView = mBinding.root

        init()

        return fragmentView
    }

    private fun init() {
        //view model
        registerViewModel()

        //buttons
        fragmentView.btn_upload_image.setOnClickListener(this)
        fragmentView.btn_next.setOnClickListener(this)
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(activity!!).get(PropertyViewModel::class.java)
        mBinding.property = viewModel.property
    }

    override fun onClick(v: View) {
        if(v.equals(fragmentView.btn_next)){
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_property, StepThreeFragment())
                .addToBackStack("StepTwo")
                .commit()
        }else {
            val uploadOptions = UploadOptionsFragment("property_")
            uploadOptions.setImageAddedListener(this)
            uploadOptions.show(activity!!.supportFragmentManager, Config.TAG_SHOW_UPLOAD_OPTIONS_FRAGMENT)
        }
    }

    override fun onImageAddedFromCamera(img: Bitmap?, uri: Uri?, path: String) {
        fragmentView.img_view.setImageBitmap(img)
        viewModel.setImagePath(path)
    }

    override fun onImageAddedFromGallery(uri: Uri?, path: String) {
        fragmentView.img_view.setImageURI(uri)
        viewModel.setImagePath(path)
    }
}