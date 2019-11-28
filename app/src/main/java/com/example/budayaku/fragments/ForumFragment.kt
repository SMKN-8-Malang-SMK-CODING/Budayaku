package com.example.budayaku.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.activities.AddDataForumActivity
import com.example.budayaku.activities.LoginActivity
import com.example.budayaku.adapters.DataForumAdapter
import com.example.budayaku.databases.DataForum
import com.example.budayaku.utils.StringSimilarity
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

    private val listForum = ArrayList<DataForum>()

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
            val notFound =
                "https://firebasestorage.googleapis.com/v0/b/budayaku-6298f.appspot.com/o/9-93669_pictures-of-questions-marks-png-image-blue-question.png?alt=media&token=83f74b0f-d9b0-4000-a0ae-1b84767de13f"

            Glide.with(this).load(notFound)
                .apply(RequestOptions())
                .into(civ_notFound)

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

            sv_forum.apply {
                setOnSearchClickListener {
                    tv_forum.visibility = View.GONE
                }

                setOnCloseListener {
                    tv_forum.visibility = View.VISIBLE
                    loadDataForum(true)
                    false
                }

                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchForum(query!!)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
//                        Toast.makeText(context, "change : $newText", Toast.LENGTH_SHORT).show()
                        searchForum(newText!!)
                        return false
                    }

                })
            }

            loadDataForum(true)
        }
    }

    override fun onResume() {
        super.onResume()

        if (currentUser != null) {
            loadDataForum(true)
        }
    }

    private fun searchForum(query: String) {
        if (listForum.size <= 0) {
            loadDataForum(false, query)
            return
        }

        val matchedForum = ArrayList<DataForum>()

        for (forum in listForum) {
            val score = StringSimilarity.similarity(forum.topik, query)

            if (score >= 0.25f) {
                matchedForum.add(forum)
            }
        }

        dataForumAdapter.setModule(matchedForum)
    }

    private fun loadDataForum(firstLoad: Boolean = false, query: String = "") {
        firestore.collection("forum").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { _, error ->
                if (error != null) {
                    Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                firestore.collection("forum").orderBy("timestamp", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener {
                        listForum.clear()
                        listForum.addAll(it.toObjects(DataForum::class.java))

                        if (firstLoad) {
                            dataForumAdapter.setModule(listForum)
                        } else {
                            searchForum(query)
                        }
                    }
            }
    }
}
