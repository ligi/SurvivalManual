package org.ligi.survivalmanual.functions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchTest {
    @Test
    fun `excerpt uses case insensitive matches`() {
        val text = "This is a Sample text about survival readiness."
        val term = "sample"

        val excerpt = getExcerpt(text, term)

        assertTrue(excerpt.contains("Sample", ignoreCase = false), "Excerpt should contain the original casing of the match")
    }

    @Test
    fun `excerpt handles text without spaces`() {
        val text = "SurvivalManual"
        val term = "manual"

        val excerpt = getExcerpt(text, term)

        assertEquals(text, excerpt, "Excerpt should fall back to the available text when no spaces are present")
    }
}
