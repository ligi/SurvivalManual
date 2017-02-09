package org.ligi.survivalmanual.functions

import android.support.annotation.VisibleForTesting
import org.ligi.survivalmanual.model.SearchResult
import org.ligi.survivalmanual.model.SurvivalContent

private val EXCERPT_SIZE = 100

@VisibleForTesting
fun getExcerpt(text: String, term: String) :String {
    val index = text.indexOf(term)
    val rough = text.substring(Math.max(index - EXCERPT_SIZE, 0)..Math.min(index + EXCERPT_SIZE, text.lastIndex))
    return rough.substring(rough.indexOf(" ")..rough.lastIndexOf(" "))
}

fun search(content: SurvivalContent, searchTerm: String)
        = content.getAllFiles()
        .associate { it to content.getMarkdown(it)!! }
        .filter { it.value.contains(searchTerm) }
        .map {
            SearchResult(it.key, getExcerpt(it.value, searchTerm))
        }

