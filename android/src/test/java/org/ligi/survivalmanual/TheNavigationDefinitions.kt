package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.getTitleResByURL

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(getTitleResByURL("02")).isEqualTo(R.string.psychology)
        assertThat(getTitleResByURL("a")).isEqualTo(R.string.kits)
        assertThat(getTitleResByURL("a#foo")).isEqualTo(R.string.kits)
    }
}