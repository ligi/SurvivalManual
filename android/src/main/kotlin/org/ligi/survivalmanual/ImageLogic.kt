package org.ligi.survivalmanual

fun linkImagesInMarkDown(markdown: String): String {
    return markdown.replace(Regex("!\\[([^\\]]+)\\]\\(([^)]+)\\)"), { matchResult: MatchResult ->
        "[" + matchResult.value + "](" + matchResult.groupValues[2] + ")"
    })
}

fun isImage(fileName: String) = fileName.endsWith(".png") || fileName.endsWith(".jpg")
