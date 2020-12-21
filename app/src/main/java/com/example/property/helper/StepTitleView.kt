package com.example.property.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.property.R
import kotlinx.android.synthetic.main.step_title_view.view.*

class StepTitleView(mContext: Context, var attr: AttributeSet): LinearLayout(mContext, attr) {

    private var imgView: ImageView
    private var textView: TextView

    init {
        inflate(mContext, R.layout.step_title_view, this)
        imgView = img_view_step
        textView = text_view_step

        val attributes = mContext.obtainStyledAttributes(attr, R.styleable.step_title_view)
        imgView.setImageDrawable(attributes.getDrawable(R.styleable.step_title_view_stepImgSrc))
        textView.setText(attributes.getString(R.styleable.step_title_view_stepTitle))
    }
}