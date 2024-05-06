package com.example.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.KakaoMapInfo
import com.example.presenter.databinding.ItemBookmarkBinding

class BookmarkAdapter(private val onItemClick: (KakaoMapInfo) -> Unit) :
    ListAdapter<KakaoMapInfo, BookmarkAdapter.BookmarkViewHolder>(
        BookmarkDiffCallback()
    ) {

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
        fun bind(document: KakaoMapInfo, onItemClick: (KakaoMapInfo) -> Unit) {
            with(binding) {
                item = document
                root.setOnClickListener {
                    onItemClick(document)
                }
            }
        }

    }

}

class BookmarkDiffCallback : DiffUtil.ItemCallback<KakaoMapInfo>() {
    override fun areItemsTheSame(oldItem: KakaoMapInfo, newItem: KakaoMapInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: KakaoMapInfo, newItem: KakaoMapInfo): Boolean {
        return oldItem == newItem
    }
}