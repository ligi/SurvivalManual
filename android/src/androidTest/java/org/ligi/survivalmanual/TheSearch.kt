package org.ligi.survivalmanual

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.functions.CaseInsensitiveSearch
import org.ligi.survivalmanual.functions.getExcerpt

@RunWith(AndroidJUnit4::class)
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