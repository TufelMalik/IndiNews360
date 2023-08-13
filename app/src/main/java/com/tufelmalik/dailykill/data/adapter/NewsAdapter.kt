package com.tufelmalik.dailykill.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.model.Article

class NewsAdapter(private var newsList : List<Article>,private var context : Context )
    :RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){


    class NewsViewHolder(binding : View) : RecyclerView.ViewHolder(binding){
        var title : TextView = binding.findViewById(R.id.txtTitleLayout)
        var source : TextView = binding.findViewById(R.id.tvSource)
        var image : ImageView = binding.findViewById(R.id.ivArticleImageLayout)
        var description : TextView = binding.findViewById(R.id.txtDescriptionLayout)
        var published : TextView = binding.findViewById(R.id.tvPublishedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        var layout = LayoutInflater.from(context)
            .inflate(R.layout.news_layout,parent,false)
        return NewsViewHolder(layout)
    }

    override fun onBindViewHolder(binding : NewsViewHolder, position: Int) {
        val data = newsList[position]
        binding.title.text = data.title
        binding.source.text = data.source.toString()
        Glide.with(context).load(data.urlToImage).into(binding.image)
        binding.description.text = data.description
        binding.published.text = data.publishedAt
    }

    override fun getItemCount() = newsList.size
}