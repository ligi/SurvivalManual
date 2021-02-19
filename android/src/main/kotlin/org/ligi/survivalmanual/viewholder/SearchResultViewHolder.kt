package org.ligi.survivalmanual.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ligi.survivalmanual.databinding.SearchResultBinding

class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val binding: SearchResultBinding = SearchResultBinding.bind(view)
    val titleTextView: TextView by lazy { binding.searchResultTitle }
    val teaserTextView: TextView by lazy { binding.searchResultText }
}