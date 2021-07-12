package com.scaler.microblogs.ui.tags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scaler.microblogs.databinding.ItemTagsBinding

class TagsAdapter: RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    private var onItemClick: ((String) -> Unit)? = null

    inner class TagsViewHolder(private val binding: ItemTagsBinding):
    RecyclerView.ViewHolder(binding.root) {

        private val tagHolder = binding.tag

        fun bind(tag: String) {

            tagHolder.text = "#$tag"
        }
    }

    fun submitList(tags: List<String>?) {

        differ.submitList(tags)
    }

    fun setOnItemClick(onItemClick: (String) -> Unit) {

        this.onItemClick = onItemClick
    }

    private val RECYCLER_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, RECYCLER_COMPARATOR)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {

        val binding = ItemTagsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return TagsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {

        val tag = differ.currentList[position]
        holder.bind(tag)
        holder.itemView.setOnClickListener {
            onItemClick?.let { it1 -> it1(tag) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}