package com.example.property.ui

import android.view.View

interface AdapterListener {
    fun onDeleteClicked(view: View, position: Int)
}