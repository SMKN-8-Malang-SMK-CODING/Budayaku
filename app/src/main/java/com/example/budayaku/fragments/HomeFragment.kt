package com.example.budayaku.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budayaku.R
import com.example.budayaku.adapters.Adapter
import com.example.budayaku.adapters.ModuleAdapter
import com.example.budayaku.databases.ModulPopular
import com.example.budayaku.databases.Module
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val firestore = Firebase.firestore

    private lateinit var moduleAdapter: ModuleAdapter

    private val listModuls = arrayListOf(
        ModulPopular("Jawa Timur"),
        ModulPopular("Yogyakarta"),
        ModulPopular("Jakarta"),
        ModulPopular("Bali"),
        ModulPopular("Banten")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moduleAdapter = ModuleAdapter(this.context!!)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

            adapter = Adapter(listModuls)
        }

        rv_itemModul.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            adapter = moduleAdapter
            isNestedScrollingEnabled = false
        }

        loadModuleData()
    }

    private fun loadModuleData() {
        firestore.collection("daerah").orderBy("id_daerah").get()
            .addOnSuccessListener {
                val daerah: List<Module> = it.toObjects(Module::class.java)
                moduleAdapter.setModul(daerah as ArrayList<Module>)
                loading_modul.visibility = View.GONE
            }
    }
}
