package com.example.property.ui.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.property.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var adapter: SignUpAcctAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        adapter = SignUpAcctAdapter(supportFragmentManager)
        adapter.setFragments()
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
    }
}