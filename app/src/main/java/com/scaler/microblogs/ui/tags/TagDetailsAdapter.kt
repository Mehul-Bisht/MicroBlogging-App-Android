package com.scaler.microblogs.ui.tags

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.shape.CornerFamily
import com.scaler.libconduit.models.Article
import com.scaler.libconduit.responses.MultipleArticleResponse
import com.scaler.microblogs.R
import com.scaler.microblogs.databinding.ItemFeedBinding
import com.scaler.microblogs.databinding.ItemTagsBinding
import java.util.*

class TagDetailsAdapter(
    private val context: Context
): RecyclerView.Adapter<TagDetailsAdapter.TagDetailsViewHolder>() {

    private var onItemClick: ((Article) -> Unit)? = null

    inner class TagDetailsViewHolder(private val binding: ItemFeedBinding):
    RecyclerView.ViewHolder(binding.root) {

        private val title = binding.title
        private val description = binding.description
        private val body = binding.body
        private val fav = binding.favourite
        private val favCount = binding.favCount
        private val authorName = binding.authorName
        private val authorImage = binding.authorImage

        fun bind(article: Article) {

            title.text = article.title
            description.text = article.description
            body.text = article.body
            authorName.text = article.author?.username

            val radius: Float = context.resources.getDimension(R.dimen.image_corner_radius)

            val shapeAppearanceModelObj = authorImage.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()

            authorImage.shapeAppearanceModel = shapeAppearanceModelObj

            favCount.text = article.favoritesCount.toString()

            article.author?.image?.let {

                Glide.with(context)
                    .load(it)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(authorImage)
            }

            var isFav = false

            fav.setOnClickListener {

                if (!isFav) {
                    fav.setImageDrawable(context.getDrawable(R.drawable.heart))
                } else {
                    fav.setImageDrawable(context.getDrawable(R.drawable.heart_outline))
                }

                isFav = !isFav
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagDetailsViewHolder {

        val binding = ItemFeedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return TagDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagDetailsViewHolder, position: Int) {

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