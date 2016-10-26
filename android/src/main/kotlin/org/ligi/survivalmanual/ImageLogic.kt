package org.ligi.survivalmanual

object ImageLogic {
    fun linkImagesInMarkDown(markdown: String) : String {
        return markdown.replace(Regex("!\\[([^\\]]+)\\]\\(([^)]+)\\)"), { matchResult : MatchResult ->
            "[" + matchResult.value + "](" + matchResult.groupValues[2] + ")"
        })
    }

    fun isImage(fileName : String ) = fileName.endsWith(".png") || fileName.endsWith(".jpg")
}