package com.example.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_postactivity.view.*
import kotlinx.android.synthetic.main.recylerview.view.*

class Postadapter( val postList:ArrayList<Post> ) :RecyclerView.Adapter<Postadapter.PostHolder>() {

    class  PostHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recylerview,parent,false)
        return PostHolder(view)

    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.recemail.text=postList[position].Kullaniciemail
        holder.itemView.yorunrec.text=postList[position].Kullaniciyorum
        Picasso.get().load(postList[position].Gorselurl).into(holder.itemView.recphoto)
    }

    override fun getItemCount(): Int {
        return postList.size

    }
}