package com.example.budayaku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.databases.DataForum
import kotlinx.android.synthetic.main.list_data_forum.view.*

class DataForumAdapter(private val context: Context) :
    RecyclerView.Adapter<DataForumAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    private var list = ArrayList<DataForum>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.list_data_forum,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.tv_forumUsername.text = list[position].username
        holder.itemView.tv_forumTopik.text = list[position].topik

        Glide.with(context).load(list[position].user_avatar)
            .apply(RequestOptions())
            .into(holder.itemView.civ_user)

        var date = ""
        val year = list[position].timestamp?.year.toString()

        date += list[position].timestamp?.date.toString() + "/"
        date += list[position].timestamp?.month.toString() + "/"
        date += year.substring(year.length - 2)

        holder.itemView.tv_forumDate.text = date
    }

    fun setModule(item: ArrayList<DataForum>) {
        this.list.clear()
        this.list.addAll(item)
        notifyDataSetChanged()
    }

}