package com.example.budayaku.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.budayaku.R
import com.example.budayaku.databases.SubModuleItem
import kotlinx.android.synthetic.main.list_sub_module_item.view.*

class ItemSubModuleAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemSubModuleAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    private var list = ArrayList<SubModuleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.list_sub_module_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.tv_subModuleItem_name.text = list[position].name

        Glide.with(context).load(list[position].image_url)
            .apply(RequestOptions())
            .into(holder.itemView.civ_subModuleItem_image)

//        holder.itemView.setOnClickListener {
//            listItem.id_item = list[position].id_item
//            listItem.name = list[position].name
//            listItem.info = list[position].info
//            listItem.image_url = list[position].image_url
//        }
    }

    fun setModule(item: ArrayList<SubModuleItem>) {
        this.list.clear()
        this.list.addAll(item)
        notifyDataSetChanged()
    }
}