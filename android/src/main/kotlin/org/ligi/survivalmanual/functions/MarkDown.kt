package org.ligi.survivalmanual.functions

import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser


private val flavour = CommonMarkFlavourDescriptor()

fun convertMarkdownToHtml(input: String): String {
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(input)
    return HtmlGenerator(input, parsedTree, flavour).generateHtml()
}