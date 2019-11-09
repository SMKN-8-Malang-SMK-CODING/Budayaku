package com.example.budayaku.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budayaku.R
import com.example.budayaku.activities.AddDataForumActivity
import com.example.budayaku.activities.LoginActivity
import com.example.budayaku.adapters.DataForumAdapter
import com.example.budayaku.databases.DataForum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account_not_found.*
import kotlinx.android.synthetic.main.fragment_forum.*

/**
 * A simple [Fragment] subclass.
 */
class ForumFragment : Fragment() {

    private var currentUser: FirebaseUser? = null
    private val firestore = Firebase.firestore
    private lateinit var dataForumAdapter: DataForumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        currentUser = FirebaseAuth.getInstance().currentUser

        return if (currentUser == null) {
            inflater.inflate(R.layout.fragment_account_not_found, container, false)
        } else {
            inflater.inflate(R.layout.fragment_forum, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (currentUser == null) {
            tv_linkLoginF!!.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            currentUser = FirebaseAuth.getInstance().currentUser
            dataForumAdapter = DataForumAdapter(this.context!!)

            btn_addNewTopik.setOnClickListener {
                startActivity(Intent(activity, AddDataForumActivity::class.java))
            }

            rv_dataForum.apply {
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                setHasFixedSize(true)

                adapter = dataForumAdapter
                isNestedScrollingEnabled = false
            }

//            FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid).get()
//                .addOnSuccessListener {
//                    val data: User? = it.toObject(User::class.java)
//                    tv_forumUsername.text = data?.name
//                    tv_forumUsername2.text = data?.name
//                    Glide.with(this).load(data?.user_avatar)
//                        .apply(RequestOptions())
//                        .into(civ_user)
//                    Glide.with(this).load(data?.user_avatar)
//                        .apply(RequestOptions())
//                        .into(civ_user2)
//                }

            loadDataForum()
        }
    }

    private fun loadDataForum() {
        firestore.collection("forum").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                val item: List<DataForum> = it.toObjects(DataForum::class.java)
                dataForumAdapter.setModule(item as ArrayList<DataForum>)
            }
    }
}
