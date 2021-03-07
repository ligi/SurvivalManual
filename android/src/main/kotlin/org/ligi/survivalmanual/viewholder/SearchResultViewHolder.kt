package org.ligi.survivalmanual.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ligi.survivalmanual.R

class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView: TextView by lazy { view.findViewById(R.id.search_result_title) }
    val teaserTextView: TextView by lazy { view.findViewById(R.id.search_result_text) }
}