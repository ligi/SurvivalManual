package org.ligi.survivalmanual.model

import com.chibatching.kotpref.KotprefModel

object VisitedURLStore : KotprefModel() {

    private var serialized by stringPref()

    fun getAll() = serialized.split(",")

    fun add(url: String) {
        if (!getAll().contains(url)) {
            serialized = getAll().plus(url).joinToString(",")
        }
    }
}
