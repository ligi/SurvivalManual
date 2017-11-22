package org.ligi.survivalmanual.functions

import android.support.annotation.VisibleForTesting
import org.ligi.survivalmanual.model.SearchResult
import org.ligi.survivalmanual.model.SurvivalContent

private const val EXCERPT_SIZE = 100

@VisibleForTesting
fun getExcerpt(text: String, term: String): String {
    val index = text.indexOf(term)
    val rough = text.substring(Math.max(index - EXCERPT_SIZE, 0)..Math.min(index + EXCERPT_SIZE, text.lastIndex))
    return rough.substring(rough.indexOf(" ")..rough.lastIndexOf(" "))
}

interface Search {
    val term: String

    fun isInContent(content: String): Boolean
}

class CaseInsensitiveSearch(override val term: String) : Search {
    private val escapedTerm = Regex.escape(term)
    val regex = Regex("(?i)$escapedTerm")

    override fun isInContent(content: String) = content.contains(regex)
}

fun search(content: SurvivalContent, searchTerm: String) = search(content, CaseInsensitiveSearch(searchTerm))

fun search(content: SurvivalContent, search: Search)
        = content.getAllFiles()
        .associate { it to content.getMarkdown(it)!! }
        .filter { search.isInContent(it.value) }
        .map {
            SearchResult(it.key, getExcerpt(it.value, search.term))
        }

