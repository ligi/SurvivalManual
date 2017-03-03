package org.ligi.survivalmanual.model

import com.chibatching.kotpref.KotprefModel
import org.ligi.kaxt.decodeURL
import org.ligi.kaxt.encodeURL
import java.io.File

data class Bookmark(val url: String, val description: String, val excerpt: String)
object Bookmarks : KotprefModel() {

    private val bookmarks by lazy { readBookmarks() }
    private val file by lazy { File(context.filesDir, "bookmarks").apply { if (!exists()) createNewFile() } }

    fun persist(bookmark: Bookmark) {
        bookmarks.add(bookmark)

        val newText = bookmarks.map { it.url.encodeURL() + ";" + it.description.encodeURL() + ";" + it.excerpt.encodeURL() }.joinToString ("\n")
        file.writeText(newText)
    }

    fun getAll() = bookmarks.toList()

    fun readBookmarks(): MutableList<Bookmark>
            = file.readLines()
            .map { it.split(";") }
            .map { Bookmark(it[0].decodeURL(), it[1].decodeURL(), it[2].decodeURL()) }
            .toMutableList()

}
