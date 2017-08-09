package org.ligi.survivalmanual.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.ligi.compat.HtmlCompat
import org.ligi.survivalmanual.EventTracker
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.functions.highLight
import org.ligi.survivalmanual.functions.search
import org.ligi.survivalmanual.model.SearchResult
import org.ligi.survivalmanual.model.SurvivalContent
import org.ligi.survivalmanual.model.titleResByURLMap
import org.ligi.survivalmanual.viewholder.SearchResultViewHolder

class SearchResultRecyclerAdapter(private var term: String, var survivalContent: SurvivalContent, val onClick: (url: String) -> Unit) : RecyclerView.Adapter<SearchResultViewHolder>() {

    var list: List<SearchResult> = search(survivalContent, term)

    fun changeTerm(term: String) {
        this.term = term
        list = search(survivalContent, term)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.search_result, parent, false)
        return SearchResultViewHolder(textView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val title = titleResByURLMap[list[position].file]
        if (title != null) {
            holder.titleTextView.text = holder.view.context.getString(title)
        } else {
            EventTracker.trackError("no title-res for " + list[position].file)
        }

        holder.teaserTextView.text = HtmlCompat.fromHtml(highLight(list[position].teaser, term))
        holder.itemView.setOnClickListener {
            onClick.invoke(list[holder.adapterPosition].file)
        }
    }

    override fun getItemCount() = list.size


}