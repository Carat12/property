package com.example.property.ui.property

import android.content.Intent
import android.net.sip.SipSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.property.R
import com.example.property.data.models.Property
import com.example.property.helper.toast
import com.example.property.ui.auth.AuthViewModel
import com.example.property.ui.auth.MyListener
import kotlinx.android.synthetic.main.activity_property.*
import kotlinx.android.synthetic.main.tool_bar.*

class PropertyActivity : AppCompatActivity(), MyListener{

    private lateinit var adapter: PropertyAdapter
    private lateinit var viewModel: PropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Property"
        tool_bar.setBackgroundColor(resources.getColor(R.color.rose_red))

        //recycler view
        adapter = PropertyAdapter(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        //view model
        viewModel = ViewModelProvider(this).get(PropertyViewModel::class.java)
        viewModel.getAllProperty()
        viewModel.getPropertyResult.observe(this, object : Observer<ArrayList<Property>>{
            override fun onChanged(t: ArrayList<Property>?) {
                if(t != null)
                    adapter.setData(t)
                else
                    onFailure(viewModel.getGetPropertyErrorMsg())
            }
        })

        //add property
        btn_add_property.setOnClickListener {
            startActivity(Intent(this, AddPropertyActivity::class.java))
        }
    }

    override fun onSuccess(msg: String) {

    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}