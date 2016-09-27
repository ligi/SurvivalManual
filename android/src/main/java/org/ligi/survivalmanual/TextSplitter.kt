package org.ligi.survivalmanual

import java.io.InputStream
import java.util.*

object TextSplitter {
    fun split(text: InputStream): List<String> {

        val list = ArrayList<String>()
        var current = ""

        text.bufferedReader().lineSequence().forEach {
            if (it.startsWith("**") || it.startsWith("###")) {
                list.add(current)
                current = ""
            }
            current += it + "\n"
        }

        list.add(current)

        return list
    }
}