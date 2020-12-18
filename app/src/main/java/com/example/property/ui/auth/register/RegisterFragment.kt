package com.example.property.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.data.models.User
import com.example.property.databinding.FragmentRegisterBinding
import com.example.property.helper.toast
import com.example.property.ui.MyListener
import com.example.property.ui.auth.AuthViewModel
import com.example.property.ui.auth.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment(), MyListener {
    private var acctType: String? = null
    private lateinit var mBinding: FragmentRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            acctType = it.getString(User.KEY_USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container,false)
        init(mBinding.root)
        return mBinding.root
    }

    private fun init(view: View) {
        //register view model
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.setAcctType(acctType!!)
        mBinding.viewModel = viewModel
        //observe sign up result
        viewModel.signUpResult.observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                if(t != null)
                    onSuccess(t)
                else
                    onFailure(viewModel.getSignUpError())
            }
        })

        //redirect to login
        view.btn_to_login.setOnClickListener{
            activity?.startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(accountType: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(User.KEY_USER, accountType)
                }
            }
    }

    override fun onSuccess(msg: String) {
        toast(msg)
        activity!!.startActivity(Intent(activity, LoginActivity::class.java))
    }

    override fun onFailure(msg: String) {
        toast(msg)
    }
}