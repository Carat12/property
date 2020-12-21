package com.example.property.ui.document

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.property.R
import com.example.property.data.models.Document
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import kotlinx.android.synthetic.main.activity_doc.*
import kotlinx.android.synthetic.main.tool_bar.*

class DocActivity : AppCompatActivity(), MyListener {

    private lateinit var adapter: DocAdapter
    private lateinit var viewModel: DocViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = "Document"
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = DocAdapter(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = GridLayoutManager(this, 2)

        registerViewModel()

        btn_add_doc.setOnClickListener { startActivity(Intent(this, AddDocActivity::class.java)) }
    }

    private fun registerViewModel() {
        viewModel = ViewModelProvider(this).get(DocViewModel::class.java)
        viewModel.getAllDocs()
        viewModel.getDocResult.observe(this, object : Observer<ArrayList<Document>>{
            override fun onChanged(t: ArrayList<Document>?) {
                if(t != null)
                    adapter.setData(t)
                else
                    onFailure(viewModel.getGetDocErrorMsg())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onSuccess(msg: String) {

    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}