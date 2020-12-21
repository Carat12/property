package com.example.property.ui.tenant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.databinding.ActivityAddTenantBinding
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import kotlinx.android.synthetic.main.tool_bar.*

class AddTenantActivity : AppCompatActivity(), MyListener {

    private lateinit var mBinding: ActivityAddTenantBinding
    private lateinit var viewModel: TenantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_tenant)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Add Tenant"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //view model
        viewModel = ViewModelProvider(this).get(TenantViewModel::class.java)
        mBinding.tenant = viewModel.tenant
        mBinding.viewModel = viewModel
        //observe the result of adding tenant
        viewModel.postTenantResult.observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getPostTenantErrorMsg())
            }
        })
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