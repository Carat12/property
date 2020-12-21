package com.example.property.ui.document

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.property.R
import com.example.property.data.models.Document
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_document.view.*

class DocAdapter(val mContext: Context): RecyclerView.Adapter<DocAdapter.ViewHolder>() {

    private var mList: ArrayList<Document> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(t: ArrayList<Document>) {
        mList = t
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(document: Document) {
            itemView.text_view_doc_name.text = document.name
            Picasso
                .get()
                .load(document.url)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(itemView.img_view)
        }

    }
}