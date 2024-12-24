package com.capstone.team.mysavior.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.data.remote.response.ArticlesItem
import com.capstone.team.mysavior.ui.detail.DetailArticleActivity

class HomeAdapter (private val articles: List<ArticlesItem>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>()  {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMediaCover: ImageView = itemView.findViewById(R.id.imageViewMediaCover)
        val textViewName: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewSummary: TextView = itemView.findViewById(R.id.textViewDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.textViewName.text = article.title
        holder.textViewSummary.text = article.description
        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .into(holder.imageViewMediaCover)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java).apply {
                putExtra("TITLE", article.title)
                putExtra("DESCRIPTION", article.description)
                putExtra("IMAGE_URL", article.urlToImage)
                putExtra("CONTENT", article.content?.toString() ?: "No content available")
                putExtra("PUBLISHED_AT", article.publishedAt)
                putExtra("AUTHOR", article.author)
            }
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return articles.size
    }
}