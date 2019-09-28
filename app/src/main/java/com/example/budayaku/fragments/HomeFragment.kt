package com.example.budayaku.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budayaku.R
import com.example.budayaku.adapters.Adapter
import com.example.budayaku.databases.Moduls
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val listModuls = arrayListOf(
        Moduls("Reinforce"),
        Moduls("BattleGuard"),
        Moduls("Lostenheim"),
        Moduls("Asterisk"),
        Moduls("Comberly")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

            adapter = Adapter(listModuls)
        }

        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("materials").document("jatim")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")

                    tv_jatimTitle.text = document.getString("main_title")
                    tv_jatimValue.text = document.getString("main_info")
                    tv_jatengTitle.text = document.getString("rm_joglo")
                    tv_jatengValue.text = document.getString("rm_joglo_info")
                    tv_jabarTitle.text = document.getString("wp_clurit")
                    tv_jabarValue.text = document.getString("wp_clurit_info")
                } else {
                    Log.d("notexist", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }
    }
}
