package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.functions.highLight

class TheHighLight {

    @Test
    fun testThatEscapingWorks() {
        val tested = highLight("fo)o", ")")

        assertThat(tested.length).isGreaterThan(4)
    }

    @Test
    fun testNoChangeWhenTermNotFound() {
        val tested = highLight("foo", ")")

        assertThat(tested.length).isEqualTo(3)
    }

}