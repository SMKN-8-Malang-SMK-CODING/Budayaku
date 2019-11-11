package com.example.budayaku.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_account_not_found.*

/**
 * A simple [Fragment] subclass.
 */
class QuestFragment : Fragment() {

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
            inflater.inflate(R.layout.fragment_quest, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            //TODO any activities when user not null
        }
    }
}
