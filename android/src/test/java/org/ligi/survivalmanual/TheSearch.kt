package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.functions.CaseInsensitiveSearch
import org.ligi.survivalmanual.functions.getExcerpt

class TheSearch {

    @Test
    fun testLinkingWorks() {
        assertThat(getExcerpt("foo bar yo","bar")).isEqualTo(" bar ")
        assertThat(getExcerpt("foo lala bar yo","bar")).isEqualTo(" lala bar ")
    }

    @Test
    fun testCaseInSensitiveWorks() {
        val tested = CaseInsensitiveSearch("foo")

        assertThat(tested.isInContent("FOO")).isEqualTo(true)
    }

    @Test
    fun testEscapingForCaseInsensitiveSearch() {
        val tested = CaseInsensitiveSearch("E)")

        assertThat(tested.isInContent("ESCAPE)")).isEqualTo(true)
        assertThat(tested.isInContent("NO")).isEqualTo(false)
    }

}