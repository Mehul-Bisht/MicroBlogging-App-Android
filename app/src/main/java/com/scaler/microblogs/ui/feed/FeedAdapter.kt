package com.scaler.microblogs.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scaler.libconduit.models.Article
import com.scaler.libconduit.responses.MultipleArticleResponse
import com.scaler.microblogs.databinding.ItemFeedBinding
import com.scaler.microblogs.databinding.ItemTagsBinding

class FeedAdapter: RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var onItemClick: ((Article) -> Unit)? = null

    inner class FeedViewHolder(private val binding: ItemFeedBinding):
    RecyclerView.ViewHolder(binding.root) {

        private val title = binding.title

        fun bind(article: Article) {

            title.text = article.title
        }
    }

    fun submitList(tags: List<Article>?) {

        differ.submitList(tags)
    }

    fun setOnItemClick(onItemClick: (Article) -> Unit) {

        this.onItemClick = onItemClick
    }

    private val RECYCLER_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.slug == newItem.slug

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, RECYCLER_COMPARATOR)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        val binding = ItemFeedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        val article = differ.currentList[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            onItemClick?.let { it1 -> it1(article) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}