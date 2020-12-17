package com.example.property.ui.auth.register

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.property.app.Config

class SignUpAcctAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){

    private val mList = Config.ACCOUNT_TYPE
    private val fragmentList = setFragments()//: ArrayList<RegisterFragment> = ArrayList()

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): RegisterFragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mList[position]
    }

    fun setFragments(): ArrayList<RegisterFragment>{
        val rList = ArrayList<RegisterFragment>()
        for (i in mList.indices){
            val fragment = RegisterFragment.newInstance(mList[i])
            rList.add(fragment)
        }
        return rList
    }
}