package com.example.budayaku.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.budayaku.R
import com.example.budayaku.activities.AccountNotFoundActivity
import com.example.budayaku.activities.UserAccountActivity
import com.example.budayaku.activities.UserLoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        tv_userAccount.setOnClickListener {
            if (currentUser != null) {
                startActivity(Intent(activity, UserAccountActivity::class.java))
            } else {
                startActivity(Intent(activity, AccountNotFoundActivity::class.java))
            }
        }

        tv_userLogout.setOnClickListener {
            if (currentUser != null) {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, UserLoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Account not detected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
