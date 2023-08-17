package com.tufelmalik.dailykill.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.databinding.NewsLayoutBinding
import com.tufelmalik.dailykill.databinding.NewsShimmerLayoutBinding
import com.tufelmalik.dailykill.ui.activity.NewsActivity
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class NewsAdapter(private val context: Context, private var newsList: List<Article>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_SHIMMER = 1
    }

    fun updateData(list: List<Article>) {
        newsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            VIEW_TYPE_CONTENT -> {
                val binding = NewsLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
                NewsViewHolder(binding)
            }
            VIEW_TYPE_SHIMMER -> {
                val binding = NewsShimmerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
                ShimmerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                val data = newsList[position]
                holder.bind(data)
            }
            is ShimmerViewHolder -> {
                holder.binding.shimmerContainer.startShimmer()
            }
        }
    }

    private fun settingPublishedTime(published: TextView, publishedAt: String) {
        val timestamp = publishedAt
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(timestamp)
        val formattedDate: String = outputFormat.format(date)
        published.text = formattedDate
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < newsList.size) {
            VIEW_TYPE_CONTENT
        } else {
            VIEW_TYPE_SHIMMER
        }
    }

    override fun getItemCount() = newsList.size 

    inner class NewsViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.tvTitle.text = article.title
            Glide.with(context).load(article.urlToImage).into(binding.ivArticleImage)
            binding.tvDescription.text = article.description
            settingPublishedTime(binding.tvPublishedAt, article.publishedAt)
            try {
                binding.root.setOnClickListener {
                    val intent = Intent(context, NewsActivity::class.java)
                    intent.putExtra("key", article.urlToImage.trim())
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ShimmerViewHolder(val binding: NewsShimmerLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
