package org.ligi.survivalmanual.functions


fun splitText(text: String): MutableList<String> {

    val list = mutableListOf("")

    text.lineSequence().forEach {
        if (it.startsWith("**") || it.startsWith("###")) {
            list.add("")
        }
        list[list.lastIndex] += it + "\n"
    }

    return list
}