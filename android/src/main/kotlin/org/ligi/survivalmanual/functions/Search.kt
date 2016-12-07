package org.ligi.survivalmanual.functions

import android.support.annotation.VisibleForTesting
import org.ligi.survivalmanual.model.SearchResult
import org.ligi.survivalmanual.model.SurvivalContent


@VisibleForTesting
fun getExtract(text: String, term: String) :String {
    val index = text.indexOf(term)
    val rough = text.substring(Math.max(index - 100, 0)..Math.min(index + 100, text.lastIndex))
    return rough.substring(rough.indexOf(" ")..rough.lastIndexOf(" "))
}

fun search(content: SurvivalContent, searchTerm: String)
        = content.getAllFiles()
        .associate { it to content.getMarkdown(it) }
        .filter { it.value.contains(searchTerm) }
        .map {
            SearchResult(it.key, getExtract(it.value, searchTerm))
        }

