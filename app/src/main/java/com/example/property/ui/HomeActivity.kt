package com.example.property.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.property.R
import com.example.property.helper.SessionManager
import com.example.property.ui.auth.AuthViewModel
import com.example.property.ui.auth.login.LoginActivity
import com.example.property.ui.property.PropertyActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.tool_bar.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Welcome, ${SessionManager.currentUser.name}"
        tool_bar.setBackgroundColor(Color.WHITE)
        tool_bar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(tool_bar)

        item_property.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            item_property -> startActivity(Intent(this, PropertyActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                SessionManager.getInstance(this).logout()
                finishAffinity()
            }
        }
        return true
    }
}