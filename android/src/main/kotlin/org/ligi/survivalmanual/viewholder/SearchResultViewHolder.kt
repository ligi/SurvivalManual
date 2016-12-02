package org.ligi.survivalmanual.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.ligi.survivalmanual.R

class SearchResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val titleTextView by lazy { view.findViewById(R.id.search_result_title) as TextView }
    val teaserTextView by lazy { view.findViewById(R.id.search_result_text) as TextView }
}