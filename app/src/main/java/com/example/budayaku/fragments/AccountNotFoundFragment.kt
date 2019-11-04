package com.example.budayaku.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budayaku.R
import com.example.budayaku.activities.LoginActivity
import kotlinx.android.synthetic.main.fragment_account_not_found.*

/**
 * A simple [Fragment] subclass.
 */
class AccountNotFoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_not_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_linkLoginF!!.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

}
