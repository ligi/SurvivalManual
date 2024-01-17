package org.ligi.survivalmanual

import org.junit.Assert.*
import org.junit.Test
import org.ligi.survivalmanual.functions.highLight

class TheHighLight {

    @Test
    fun testThatEscapingWorks() {
        val tested = highLight("fo)o", ")")

        assertTrue(tested.length > 4)
    }

    @Test
    fun testNoChangeWhenTermNotFound() {
        val tested = highLight("foo", ")")

        assertEquals(3, tested.length)
    }

}