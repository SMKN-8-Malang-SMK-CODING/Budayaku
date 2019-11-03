package com.example.budayaku.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.adapters.ItemSubModuleAdapter
import com.example.budayaku.databases.SubModuleItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_sub_modul.*

class DetailSubModuleActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private lateinit var itemSubModuleAdapter: ItemSubModuleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sub_modul)

        val id_daerah = intent.getIntExtra("id_daerah", 1)
        val jenis = intent.getStringExtra("jenis")

        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection(jenis!!).whereEqualTo("id_daerah", id_daerah)
        docRef.get()
            .addOnSuccessListener {

                val data = it.toObjects(SubModuleItem::class.java)

                data.forEach { document ->

                    if (document != null) {
                        loading_subModul.visibility = View.GONE
                        tv_titleSubModul.text = document.name
                        tv_infoSubModul.text = document.info
                        Glide.with(this).load(document.image_url)
                            .apply(RequestOptions())
                            .into(civ_subModule)
                    } else {
                        Log.d("notexist", "No such document")
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }

        iv_backDetail.setOnClickListener {
            onBackPressed()
        }

        itemSubModuleAdapter = ItemSubModuleAdapter(this)

        rv_itemSubModul.apply {
            layoutManager = LinearLayoutManager(
                this@DetailSubModuleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)

            adapter = itemSubModuleAdapter
        }

        loadItemSubModuleData()
    }

    private fun loadItemSubModuleData() {
        val id_daerah = intent.getIntExtra("id_daerah", 1)
        val jenis = intent.getStringExtra("jenis")

        firestore.collection(jenis!!).whereEqualTo("id_daerah", id_daerah).get()
            .addOnSuccessListener {
                val item: List<SubModuleItem> = it.toObjects(SubModuleItem::class.java)
                itemSubModuleAdapter.setModule(item as ArrayList<SubModuleItem>)
            }
    }
}
