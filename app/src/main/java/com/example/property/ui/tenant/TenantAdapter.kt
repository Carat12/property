package com.example.property.ui.tenant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.property.data.models.Tenant
import com.example.property.databinding.ItemTenantBinding
import com.example.property.helper.SessionManager
import com.example.property.ui.AdapterListener
import kotlinx.android.synthetic.main.item_tenant.view.*

class TenantAdapter(val mContextz: Context): RecyclerView.Adapter<TenantAdapter.ViewHolder>() {

    private lateinit var listener: AdapterListener
    private var mList: ArrayList<Tenant> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTenantBinding.inflate(LayoutInflater.from(mContextz), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(tenants: ArrayList<Tenant>) {
        mList = ArrayList()
        for(t in tenants){
            if(t.landlordId == SessionManager.currentUser._id)
                mList.add(t)
        }
        notifyDataSetChanged()
    }


    inner class ViewHolder(val itemBinding: ItemTenantBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bind(tenant: Tenant, position: Int) {
            itemBinding.tenant = tenant

            itemBinding.root.btn_delete_tenant.setOnClickListener {
                listener.onDeleteClicked(it, position)
            }
        }
    }
/*
    interface TenantAdapterListener{
        fun onDeleteClicked(view: View, position: Int)
    }*/

    fun setTenantAdapterListener(listener: AdapterListener){
        this.listener = listener
    }

    fun getItemData(position: Int): Tenant{
        return mList[position]
    }
}