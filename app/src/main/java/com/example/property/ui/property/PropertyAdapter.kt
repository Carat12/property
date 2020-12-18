package com.example.property.ui.property

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.property.R
import com.example.property.data.models.Property
import com.example.property.databinding.ItemPropertyBinding
import com.example.property.helper.SessionManager
import com.example.property.ui.auth.AuthViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_property.view.*

class PropertyAdapter(var mContext: Context) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    private var mList: ArrayList<Property> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemPropertyBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(propertyList: ArrayList<Property>) {
        mList = ArrayList()
        for(p in propertyList){
            if(p.userId.equals(SessionManager.currentUser._id))
                mList.add(p)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(var itemBinding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(property: Property) {
            itemBinding.property = property
            Picasso
                .get()
                .load(property.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemBinding.root.img_view)
        }
    }
}