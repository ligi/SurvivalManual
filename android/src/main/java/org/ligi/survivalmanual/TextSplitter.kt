package org.ligi.survivalmanual

import okio.BufferedSource
import java.util.*

object TextSplitter {
    fun split(text: BufferedSource): List<String> {
        val list = ArrayList<String>()
        var current = ""

        var line = text.readUtf8Line()
        while (line != null) {

            if (line.startsWith("**") || line.startsWith("###")) {
                list.add(current)
                current = ""
            }
            current += line + "\n"
            line = text.readUtf8Line()
        }

        if (!current.isEmpty()) {
            list.add(current)
        }

        return list
    }
}