package org.ligi.survivalmanual.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.search_result.view.*

class SearchResultViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val titleTextView: TextView by lazy { view.search_result_title }
    val teaserTextView: TextView by lazy { view.search_result_text }
}