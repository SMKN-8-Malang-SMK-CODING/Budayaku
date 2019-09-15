package com.example.budayaku.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budayaku.R
import com.example.budayaku.databases.Moduls
import kotlinx.android.synthetic.main.list_recent_learn.view.*

class Adapter(private val list: ArrayList<Moduls>) : RecyclerView.Adapter<Adapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_recent_learn,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.listTitle.text = list[position].name
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)
}
