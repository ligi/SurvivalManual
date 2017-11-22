package org.ligi.survivalmanual.model

import com.chibatching.kotpref.KotprefModel
import org.ligi.kaxt.decodeURL
import org.ligi.kaxt.encodeURL

object Bookmarks : KotprefModel() {

    private var serialized by stringPref()

    private val bookmarks by lazy { readBookmarks() }

    fun persist(bookmark: Bookmark) {
        bookmarks.add(bookmark)

        serialized = bookmarks.joinToString("\n") { it.url.encodeURL() + ";" + it.description.encodeURL() + ";" + it.excerpt.encodeURL() }
    }

    fun getAll() = bookmarks.toList()

    private fun readBookmarks(): MutableList<Bookmark>
            = serialized.lines()
            .map { it.split(";") }
            .map { Bookmark(it[0].decodeURL(), it[1].decodeURL(), it[2].decodeURL()) }
            .toMutableList()

}
