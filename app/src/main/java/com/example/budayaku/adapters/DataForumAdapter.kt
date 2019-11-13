package com.example.budayaku.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.activities.DetailForumActivity
import com.example.budayaku.databases.DataForum
import com.example.budayaku.databases.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.list_data_forum.view.*

class DataForumAdapter(private val context: Context) :
    RecyclerView.Adapter<DataForumAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    private val firestore = FirebaseFirestore.getInstance()
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
        firestore.collection("users").document(list[position].user_id).get()
            .addOnSuccessListener {
                val user: User = it.toObject()!!

                holder.itemView.tv_forumUsername.text = user.username
                Glide.with(context).load(user.user_avatar)
                    .apply(RequestOptions())
                    .into(holder.itemView.civ_user)
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }

        holder.itemView.tv_forumTopik.text = list[position].topik

        var date = ""
        val year = list[position].timestamp?.year.toString()

        date += list[position].timestamp?.date.toString() + "/"
        date += list[position].timestamp?.month.toString() + "/"
        date += year.substring(year.length - 2)

        holder.itemView.tv_forumDate.text = date

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailForumActivity::class.java)
            intent.putExtra("topik", list[position].topik)
            context.startActivity(intent)
        }
    }

    fun setModule(item: ArrayList<DataForum>) {
        this.list.clear()
        this.list.addAll(item)
        notifyDataSetChanged()
    }

}