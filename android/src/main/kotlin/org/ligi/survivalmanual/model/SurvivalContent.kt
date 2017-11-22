package org.ligi.survivalmanual.model

import android.content.res.AssetManager
import java.io.IOException

class SurvivalContent(private val assetManager: AssetManager) {

    fun getMarkdown(url: String) = try {
        assetManager.open(getFullMarkDownURL(url)).use { it.bufferedReader().readText() }
    } catch (e: IOException) {
        null
    }

    fun hasFile(url: String) = try {
        assetManager.open("md/" + url) != null
    } catch (e: IOException) {
        false
    }

    fun getAllFiles() = assetManager.list("md").filter { it.endsWith(".md") }.map { it.replace(".md", "") }

    private fun getFullMarkDownURL(url: String) = "md/$url.md"
}