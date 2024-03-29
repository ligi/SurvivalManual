package org.ligi.survivalmanual

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.model.SurvivalContent
import org.ligi.survivalmanual.model.navigationEntryMap
import org.ligi.survivalmanual.model.titleResByURLMap

@RunWith(AndroidJUnit4::class)
class TheSurvivalContent {

    val survivalContent = SurvivalContent(InstrumentationRegistry.getInstrumentation().targetContext.assets)

    @Test
    fun weCanLoadAllEntriesFromNavigation() {
        navigationEntryMap.forEach { navEntryWithId ->
            val url = navEntryWithId.entry.url
            val tested = survivalContent.getMarkdown(url)
            if (tested == null) {
                fail("could not load $url")
            }

            val flavour = CommonMarkFlavourDescriptor()
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(tested!!)

            val elementCollectingVisitor = ElementCollectingVisitor(MarkdownElementTypes.LINK_DESTINATION)
            elementCollectingVisitor.visitNode(parsedTree)

            val unresolvedLinks = elementCollectingVisitor.elementList.asSequence()
                    .map { element -> element.getTextInNode(tested).toString() }
                    .filter { link -> !link.startsWith("#") }
                    .filter { link -> !link.startsWith("http") }
                    .filter { link -> !link.endsWith(".vd") }
                    .filter { link -> !survivalContent.hasFile(link) }
                    .filterNot { link -> survivalContent.getMarkdown(link) != null && titleResByURLMap.containsKey(link) }
                    .toSet()

            assertEquals(setOf("SolarUSBCharger", "CampStoveUSB", "HandCrankUSB", "CarUSBCharger"), unresolvedLinks)
            // TOOD Should be empty, but references to products are still in the data.
        }
    }

    class ElementCollectingVisitor(val type: IElementType) : RecursiveVisitor() {

        var elementList = mutableListOf<ASTNode>()

        override fun visitNode(node: ASTNode) {
            if (node.type == type) {
                elementList.add(node)
            }
            super.visitNode(node)
        }

    }


    @Test
    fun weGetNullForUnknownURL() {
        val tested = survivalContent.getMarkdown("YOLO")
        assertNull(tested)
    }

}
