package com.example.budayaku.fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.activities.AccountNotFoundActivity
import com.example.budayaku.activities.UserAccountActivity
import com.example.budayaku.activities.UserLoginActivity
import com.example.budayaku.databases.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).get()
                .addOnSuccessListener {
                    val data: User? = it.toObject(User::class.java)
                    tv_userAccount.text = data?.name
                    tv_userLocation.text = data?.location
                    Glide.with(this).load(data?.user_avatar)
                        .apply(RequestOptions())
                        .into(civ_account)
                }
        }

        show_user.setOnClickListener {
            if (currentUser != null) {
                startActivity(Intent(activity, UserAccountActivity::class.java))
            } else {
                startActivity(Intent(activity, AccountNotFoundActivity::class.java))
            }
        }

        tv_userLogout.setOnClickListener {
            val builder = AlertDialog.Builder(activity)

            builder.setMessage("Apa anda yakin untuk keluar akun?")
            builder.setPositiveButton("Ya") { _, _ ->
                if (currentUser != null) {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(activity, UserLoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "Account not detected", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Tidak") { _, _ -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}
