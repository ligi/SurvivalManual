package org.ligi.survivalmanual.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_result.view.*

class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView: TextView by lazy { view.search_result_title }
    val teaserTextView: TextView by lazy { view.search_result_text }
}