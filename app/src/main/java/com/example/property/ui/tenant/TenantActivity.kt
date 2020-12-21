package com.example.property.ui.tenant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.property.R
import com.example.property.data.models.Tenant
import com.example.property.helper.toast
import com.example.property.ui.AdapterListener
import com.example.property.ui.MyListener
import kotlinx.android.synthetic.main.activity_tenant.*
import kotlinx.android.synthetic.main.tool_bar.*

class TenantActivity : AppCompatActivity(), MyListener, AdapterListener {

    private lateinit var adapter: TenantAdapter
    private lateinit var viewModel: TenantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)

        init()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllTenants()
    }

    private fun init() {
        //too bar
        tool_bar.title = "Tenants"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = TenantAdapter(this)
        adapter.setTenantAdapterListener(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        registerViewModel()

        //add tenant
        btn_add_tenant.setOnClickListener {
            startActivity(Intent(this, AddTenantActivity::class.java))
        }
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(this).get(TenantViewModel::class.java)
        viewModel.getAllTenants()

        viewModel.getTenantResult.observe(this, object : Observer<ArrayList<Tenant>> {
            override fun onChanged(t: ArrayList<Tenant>?) {
                if (t != null) {
                    //progress bar
                    progress_bar.visibility = View.GONE
                    adapter.setData(t)
                } else
                    onFailure(viewModel.getGetTenantErrorMsg())
            }
        })

        viewModel.deleteTenantResult.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getDeleteTenantErrorMsg())
            }
        })
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        viewModel.getAllTenants()
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onDeleteClicked(view: View, position: Int) {
        val tenant = adapter.getItemData(position)
        AlertDialog.Builder(this)
            .setTitle("Delete Tenant")
            .setMessage("Are you sure to delete tenant ${tenant.name}?")
            .setPositiveButton("Delete") { dialog, which -> viewModel.deleteTenant(tenant._id!!) }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .show()
    }
}