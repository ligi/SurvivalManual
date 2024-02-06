package org.ligi.survivalmanual

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.functions.highLight

@RunWith(AndroidJUnit4::class)
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