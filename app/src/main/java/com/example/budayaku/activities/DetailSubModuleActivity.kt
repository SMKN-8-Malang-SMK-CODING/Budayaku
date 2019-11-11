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
import com.example.budayaku.databases.ListItemSubModule
import com.example.budayaku.databases.SubModuleItem
import com.example.budayaku.utils.RecyclerItemClickListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_detail_sub_modul.*


class DetailSubModuleActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore
    private lateinit var itemSubModuleAdapter: ItemSubModuleAdapter

    val listItem = ArrayList<SubModuleItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_sub_modul)

        val id_daerah = intent.getIntExtra("id_daerah", 1)
        val jenis = intent.getStringExtra("jenis")

        val db = FirebaseFirestore.getInstance()

        loadCurrentSubModulData(db, jenis!!, id_daerah, 0)

        iv_backDetail.setOnClickListener {
            onBackPressed()
        }

        itemSubModuleAdapter = ItemSubModuleAdapter(this)

        rv_itemSubModule.apply {
            layoutManager = LinearLayoutManager(
                this@DetailSubModuleActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)

            adapter = itemSubModuleAdapter
        }

        rv_itemSubModule.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                rv_itemSubModule,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val item = listItem[position]

                        loadCurrentSubModulData(db, jenis, id_daerah, item.id_item)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val item = listItem[position]

                        loadCurrentSubModulData(db, jenis, id_daerah, item.id_item)
                    }
                })
        )

        loadItemSubModuleData()
    }

    private fun loadCurrentSubModulData(
        db: FirebaseFirestore,
        jenis: String,
        id_daerah: Int,
        id_item: Int
    ) {
        val docRef = db.collection(jenis).whereEqualTo("id_daerah", id_daerah)

        docRef.get()
            .addOnSuccessListener {
                val data = it.toObjects(ListItemSubModule::class.java)

                if (id_item == 0) {
                    showData(data[0])
                }

                data.forEach { document ->
                    if (document != null) {
                        if (document.id_item == id_item) {
                            showData(document)
                            return@addOnSuccessListener
                        }
                    } else {
                        Log.d("notexist", "No such document")
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d("errordb", "get failed with ", exception)
            }
    }

    private fun showData(subModule: ListItemSubModule) {
        loading_subModul.visibility = View.GONE

        tv_titleSubModule.text = subModule.name
        tv_infoSubModule.text = subModule.info

        Glide.with(this).load(subModule.image_url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
            .into(iv_backgroundSubModule)

        Glide.with(this).load(subModule.image_url)
            .apply(RequestOptions())
            .into(civ_subModule)

    }

    private fun loadItemSubModuleData() {
        val id_daerah = intent.getIntExtra("id_daerah", 1)
        val jenis = intent.getStringExtra("jenis")

        firestore.collection(jenis!!).whereEqualTo("id_daerah", id_daerah).get()
            .addOnSuccessListener {
                listItem.clear()
                listItem.addAll(it.toObjects(SubModuleItem::class.java))

                itemSubModuleAdapter.setModule(listItem)
            }
    }
}
