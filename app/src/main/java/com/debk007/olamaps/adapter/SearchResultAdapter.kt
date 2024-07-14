package com.debk007.olamaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.debk007.olamaps.databinding.ItemSearchResultBinding
import com.debk007.olamaps.model.autocomplete.Prediction

class SearchResultAdapter(
    private val onSearchResultClick: (Pair<Double, Double>) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private val items: MutableList<Prediction> = mutableListOf()

    inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.searchResultText.text = items[adapterPosition].description

            binding.searchResultText.setOnClickListener {
                onSearchResultClick(items[adapterPosition].geometry.location.lat to items[adapterPosition].geometry.location.lng)
                updateData(emptyList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind()
    }

    fun updateData(newItemList: List<Prediction>) {
        items.clear()
        items.addAll(newItemList)
        notifyDataSetChanged()
    }
}