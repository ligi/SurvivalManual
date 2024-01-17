package org.ligi.survivalmanual

import org.junit.Assert.*
import org.junit.Test
import org.ligi.survivalmanual.functions.CaseInsensitiveSearch
import org.ligi.survivalmanual.functions.getExcerpt

class TheSearch {

    @Test
    fun testLinkingWorks() {
        assertEquals(" bar ", getExcerpt("foo bar yo","bar"))
        assertEquals(" lala bar ", getExcerpt("foo lala bar yo","bar"))
    }

    @Test
    fun testCaseInSensitiveWorks() {
        val tested = CaseInsensitiveSearch("foo")

        assertTrue(tested.isInContent("FOO"))
    }

    @Test
    fun testEscapingForCaseInsensitiveSearch() {
        val tested = CaseInsensitiveSearch("E)")

        assertTrue(tested.isInContent("ESCAPE)"))
        assertFalse(tested.isInContent("NO"))
    }

}