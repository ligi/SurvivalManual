package org.ligi.survivalmanual.functions

import androidx.annotation.VisibleForTesting
import org.ligi.survivalmanual.model.SearchResult
import org.ligi.survivalmanual.model.SurvivalContent
import kotlin.math.max

private const val EXCERPT_SIZE = 100

@VisibleForTesting
fun getExcerpt(text: String, term: String): String {
    val index = text.indexOf(term, ignoreCase = true)
    if (index == -1) {
        return text.take(EXCERPT_SIZE * 2).trim()
    }

    val startBound = max(index - EXCERPT_SIZE, 0)
    val endBound = (index + term.length + EXCERPT_SIZE).coerceAtMost(text.length)

    val excerptStart = text.lastIndexOf(' ', startBound).takeIf { it >= 0 }?.plus(1) ?: startBound
    val excerptEnd = text.indexOf(' ', endBound).takeIf { it >= 0 } ?: endBound

    if (excerptEnd > excerptStart) {
        return text.substring(excerptStart, excerptEnd).trim()
    }

    return text.substring(startBound, endBound).trim()
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

fun search(content: SurvivalContent, search: Search) = content.getAllFiles()
        .associateWith { content.getMarkdown(it)!! }
        .filter { search.isInContent(it.value) }
        .map {
            SearchResult(it.key, getExcerpt(it.value, search.term))
        }

