package com.example.property.ui.property

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.property.R
import com.example.property.data.models.Property
import com.example.property.data.models.Tenant
import com.example.property.databinding.ItemPropertyBinding
import com.example.property.helper.SessionManager
import com.example.property.ui.AdapterListener
import com.example.property.ui.auth.AuthViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_property.view.*

class PropertyAdapter(var mContext: Context) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    private lateinit var listener: AdapterListener
    private var mList: ArrayList<Property> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemPropertyBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(propertyList: ArrayList<Property>) {
        mList = ArrayList()
        if (SessionManager.isTenant())
            mList = propertyList
        else {
            for (p in propertyList) {
                if (p.userId.equals(SessionManager.currentUser._id))
                    mList.add(p)
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(var itemBinding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(property: Property, position: Int) {
            property.setAddressLine()
            itemBinding.property = property
            itemBinding.root.text_view_mortgage_true.isVisible = property.mortageInfo!!
            itemBinding.root.text_view_mortgage_false.isVisible = !property.mortageInfo!!
            Picasso
                .get()
                .load(property.image)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemBinding.root.img_view)

            itemBinding.root.btn_delete_property.setOnClickListener {
                listener.onDeleteClicked(it, position)
            }
        }
    }

    fun setAdapterListener(listener: AdapterListener) {
        this.listener = listener
    }

    fun getItemData(position: Int): Property {
        return mList[position]
    }
}


/*val text_view_mortgage = itemBinding.root.text_view_mortgage
text_view_mortgage.setBackgroundColor(mContext.resources.getColor(
    if(property.mortageInfo!!) R.color.light_rose
else R.color.light_serenity
))
text_view_mortgage.setTextColor(mContext.resources.getColor(
    if(property.mortageInfo!!) R.color.rose_red
else R.color.dark_serenity
))
text_view_mortgage.compoundDrawables[0].setTint(mContext.resources.getColor(
    if(property.mortageInfo!!) R.color.rose_red
else R.color.dark_serenity
))
text_view_mortgage.setCompoundDrawables(ContextCompat.getDrawable(mContext,
    if(property.mortageInfo!!) R.drawable.ic_baseline_check_24
    else R.drawable.ic_baseline_clear_24), null, null, null)*/