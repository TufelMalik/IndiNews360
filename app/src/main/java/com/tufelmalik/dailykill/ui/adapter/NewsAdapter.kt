package com.tufelmalik.dailykill.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.classes.Constants.setDate2Days
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.databinding.NewsLayoutBinding
import com.tufelmalik.dailykill.databinding.ShimmerLayoutBinding
import com.tufelmalik.dailykill.ui.activity.NewsActivity

class NewsAdapter(
    private val context: Context,
    private var newsList: List<Article>,
    private var category: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_SHIMMER = 1
    }

    private var dataLoaded = false

    fun updateData(data: List<Article>) {
        newsList = data
        dataLoaded = true
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
                val binding = ShimmerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
                ShimmerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                if (position < newsList.size) {
                    val data = newsList[position]
                    holder.bind(data)
                }
            }
            is ShimmerViewHolder -> {
                holder.startShimmer()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < newsList.size) {
            VIEW_TYPE_CONTENT
        } else {
            VIEW_TYPE_SHIMMER
        }
    }

    override fun getItemCount(): Int {
        // Return the total number of items, including both data and shimmer items
        val dataCount = if (dataLoaded) newsList.size else 0
        return dataCount + 10 // Change 10 to the number of shimmer items you want to display
    }

    inner class NewsViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.imgShareNews.setOnClickListener {
                Constants.shareNews(context, article)
            }

            binding.tvTitle.text = article.title
            if (article.author == null) {
                binding.txtAuthorLayout.text = context.getString(R.string.indiannews) + "  |   "
            } else {
                binding.txtAuthorLayout.text = article.author + "  |   "
            }
            Glide.with(context).load(article.urlToImage).thumbnail(Glide.with(context).load(R.drawable.loading))
                .into(binding.ivArticleImage)
            binding.tvDescription.text = article.description
            setDate2Days(binding.tvPublishedAt, article.publishedAt)
            try {
                binding.root.setOnClickListener {
                    val intent = Intent(context, NewsActivity::class.java)
                    if (category == "world") {
                        intent.putExtra("category", category)
                    }
                    intent.putExtra("img", article.urlToImage)
                    intent.putExtra("title", article.title)
                    intent.putExtra("link", article.url)
                    intent.putExtra("des", article.description)
                    intent.putExtra("date", article.publishedAt)
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ShimmerViewHolder(val binding: ShimmerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun startShimmer() {
            binding.shimmerLayout.startShimmer()
        }

        fun stopShimmer() {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }
    }
}
