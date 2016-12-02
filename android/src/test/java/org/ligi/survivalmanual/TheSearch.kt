package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.functions.getExtract

class TheSearch {

    @Test
    fun testLinkingWorks() {
        assertThat(getExtract("foo bar yo","bar")).isEqualTo(" bar ")
        assertThat(getExtract("foo lala bar yo","bar")).isEqualTo(" lala bar ")
    }

}