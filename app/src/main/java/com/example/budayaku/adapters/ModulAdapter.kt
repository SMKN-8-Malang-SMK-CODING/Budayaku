package com.example.budayaku.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.activities.DetailModulActivity
import com.example.budayaku.databases.Modul
import kotlinx.android.synthetic.main.list_modul.view.*

class ModulAdapter(private val context: Context) : RecyclerView.Adapter<ModulAdapter.Holder>() {

    private val list = ArrayList<Modul>()

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.list_modul,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.tv_modulTitle.text = list[position].name
        holder.itemView.tv_modulInfo.text = list[position].info

        Glide.with(context).load(list[position].image_url)
            .apply(RequestOptions())
            .into(holder.itemView.bg_modul)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailModulActivity::class.java)
            intent.putExtra("id_daerah", list[position].id_daerah)
            context.startActivity(intent)
        }
    }

    fun setModul(modul: ArrayList<Modul>) {
        this.list.clear()
        this.list.addAll(modul)
        notifyDataSetChanged()
    }
}