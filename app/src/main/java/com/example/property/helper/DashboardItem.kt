package com.example.property.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.property.R
import kotlinx.android.synthetic.main.dashboard_item.view.*

class DashboardItem(mContext: Context, attr: AttributeSet): LinearLayout(mContext, attr) {
    val imgView: ImageView
    val textView: TextView

    init {
        inflate(mContext, R.layout.dashboard_item, this)
        imgView = img_view
        textView = text_view

        val attributes = mContext.obtainStyledAttributes(attr, R.styleable.dashboard_item)
        imgView.setImageDrawable(attributes.getDrawable(R.styleable.dashboard_item_imgSrc))
        textView.setText(attributes.getString(R.styleable.dashboard_item_title))
    }
}