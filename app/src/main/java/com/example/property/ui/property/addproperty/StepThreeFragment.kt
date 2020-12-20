package com.example.property.ui.property.addproperty

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.databinding.FragmentStepThreeBinding
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import com.example.property.ui.property.PropertyViewModel

class StepThreeFragment : Fragment(), MyListener {

    private lateinit var mBinding: FragmentStepThreeBinding
    private lateinit var fragmentView: View
    private lateinit var viewModel: PropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_three, container, false)
        fragmentView = mBinding.root

        init()

        return fragmentView
    }

    private fun init() {
        registerViewModel()
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(activity!!).get(PropertyViewModel::class.java)
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

    override fun onSuccess(msg: String) {
        toast(msg)
        activity?.finish()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}