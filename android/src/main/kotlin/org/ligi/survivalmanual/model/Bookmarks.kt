package org.ligi.survivalmanual.model

import com.chibatching.kotpref.KotprefModel
import java.net.URLDecoder
import java.net.URLEncoder

object Bookmarks : KotprefModel() {

    private var serialized by stringPref()

    private val bookmarks by lazy { readBookmarks() }

    fun persist(bookmark: Bookmark) {
        bookmarks.add(bookmark)

        serialized = bookmarks.joinToString("\n") { URLEncoder.encode(it.url, "utf-8") + ";" + URLEncoder.encode(it.description, "utf-8") + ";" + URLEncoder.encode(it.excerpt, "utf-8" )}
    }

    fun getAll() = bookmarks.toList()

    private fun readBookmarks(): MutableList<Bookmark> = serialized.lines()
            .map { it.split(";") }
            .map { Bookmark(URLDecoder.decode(it[0], "utf-8"), URLDecoder.decode(it[1], "utf-8"), URLDecoder.decode(it[2], "utf-8")) }
            .toMutableList()

}
