package org.ligi.survivalmanual.model

import android.content.res.AssetManager

class SurvivalContent(val assetManager: AssetManager) {

    fun getMarkdown(url: String) = assetManager.open(getFullMarkDownURL(url)).bufferedReader().readText()

    fun getAllFiles() = assetManager.list("md").filter { it.endsWith(".md") }.map { it.replace(".md","") }

    private fun getFullMarkDownURL(url: String) = "md/$url.md"
}