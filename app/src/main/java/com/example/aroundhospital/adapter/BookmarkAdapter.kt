package com.example.aroundhospital.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aroundhospital.databinding.ItemBookmarkBinding
import com.example.data.api.response.Document

class BookmarkAdapter(private val onItemClick: (Document) -> Unit) :
    ListAdapter<Document, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemBookmarkBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class BookmarkViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document, onItemClick: (Document) -> Unit) {
            with(binding) {
                item = document
                root.setOnClickListener {
                    onItemClick(document)
                }
            }
        }

    }

}

class BookmarkDiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}