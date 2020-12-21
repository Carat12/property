package com.example.property.ui.property

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.property.R
import com.example.property.app.Config
import com.example.property.data.models.Property
import com.example.property.helper.SessionManager
import com.example.property.helper.toast
import com.example.property.ui.AdapterListener
import com.example.property.ui.MyListener
import com.example.property.ui.property.addproperty.AddPropertyActivity
import kotlinx.android.synthetic.main.activity_property.*
import kotlinx.android.synthetic.main.tool_bar.*

class PropertyActivity : AppCompatActivity(), MyListener, AdapterListener {

    private lateinit var adapter: PropertyAdapter
    private lateinit var viewModel: PropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property)

        init()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllProperty()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Property"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //progress bar
        progress_bar.visibility = View.VISIBLE

        //recycler view
        adapter = PropertyAdapter(this)
        adapter.setAdapterListener(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        //view model
        registerViewModel()

        //add property
        btn_add_property.isVisible = !SessionManager.isTenant()
        btn_add_property.setOnClickListener {
            startActivity(Intent(this, AddPropertyActivity::class.java))
        }
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(this).get(PropertyViewModel::class.java)
        viewModel.getAllProperty()
        viewModel.getPropertyResult.observe(this, object : Observer<ArrayList<Property>> {
            override fun onChanged(t: ArrayList<Property>?) {
                if (t != null) {
                    adapter.setData(t)
                    //progress bar
                    progress_bar.visibility = View.GONE
                    //text_view_total.text = "%,d".format(t.size) + " Properties"
                } else
                    onFailure(viewModel.getGetPropertyErrorMsg())
            }
        })

        viewModel.deletePropertyResult.observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getDeletePropertyErrorMsg())
            }
        })
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        viewModel.getAllProperty()
    }

    override fun onFailure(msg: String) {
        progress_bar.visibility = View.GONE
        toast(msg)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onDeleteClicked(view: View, position: Int) {
        val property = adapter.getItemData(position)
        AlertDialog.Builder(this)
            .setTitle("Delete Property")
            .setMessage("Are you sure to delete this property")
            .setPositiveButton("Delete") { dialog, which ->
                viewModel.deleteProperty(property._id!!)
                progress_bar.visibility = View.VISIBLE
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .show()
    }
}