package com.zexly.istergenc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zexly.istergenc.R
import com.zexly.istergenc.model.Post
import kotlinx.android.synthetic.main.recycler_row.view.*

class AnaMenuRecyclerAdapter(val postList:ArrayList<Post>): RecyclerView.Adapter<AnaMenuRecyclerAdapter.PostHolder>() {

    class PostHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.itemView.recycler_row_kullanici_adi.text=postList[position].guncelKullaniciAdi
        holder.itemView.recycler_row_talep_detay.text=postList[position].talepDetayi
        holder.itemView.recycler_row_hashtag.text=postList[position].ilgiliKurum
        holder.itemView.imzaSayisi.text=postList[position].begeniSayisi

        if (postList[position].gorselUrl.equals("null"))
        {
            holder.itemView.recycler_row_imageview.visibility= View.GONE
        }
        else{
            holder.itemView.recycler_row_imageview.visibility= View.VISIBLE
        }
        Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.recycler_row_imageview)

    }
    override fun getItemCount(): Int {
        return postList.size
    }
}