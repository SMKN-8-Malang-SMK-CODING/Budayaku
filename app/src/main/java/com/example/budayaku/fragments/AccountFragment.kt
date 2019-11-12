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
import com.example.budayaku.activities.AccountActivity
import com.example.budayaku.activities.AccountNotFoundActivity
import com.example.budayaku.activities.LoginActivity
import com.example.budayaku.activities.SettingAccountActivity
import com.example.budayaku.databases.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account_not_found.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        currentUser = FirebaseAuth.getInstance().currentUser

        return if (currentUser == null) {
            inflater.inflate(R.layout.fragment_account_not_found, container, false)
        } else {
            inflater.inflate(R.layout.fragment_account, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val notFound =
                "https://firebasestorage.googleapis.com/v0/b/budayaku-6298f.appspot.com/o/9-93669_pictures-of-questions-marks-png-image-blue-question.png?alt=media&token=83f74b0f-d9b0-4000-a0ae-1b84767de13f"

            Glide.with(this).load(notFound)
                .apply(RequestOptions())
                .into(civ_notFound)

            tv_linkLoginF!!.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            iv_setting.setOnClickListener {
                val intent = Intent(activity, SettingAccountActivity::class.java)
                startActivity(intent)
            }

            FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid).get()
                .addOnSuccessListener {
                    val data: User? = it.toObject(User::class.java)
                    tv_userAccount.text = currentUser!!.displayName
                    tv_userLocation.text = data?.location
                    Glide.with(this).load(currentUser!!.photoUrl)
                        .apply(RequestOptions())
                        .into(civ_account)
                }

            show_user.setOnClickListener {
                if (currentUser != null) {
                    startActivity(Intent(activity, AccountActivity::class.java))
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
                        val intent = Intent(activity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
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
}
