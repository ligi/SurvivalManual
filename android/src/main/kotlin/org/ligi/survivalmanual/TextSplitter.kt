package org.ligi.survivalmanual

import java.io.InputStream

object TextSplitter {
    fun split(text: InputStream): MutableList<String> {

        val list = mutableListOf("")

        text.bufferedReader().lineSequence().forEach {
            if (it.startsWith("**") || it.startsWith("###")) {
                list.add("")
            }
            list[list.lastIndex] += it + "\n"
        }

        return list
    }
}