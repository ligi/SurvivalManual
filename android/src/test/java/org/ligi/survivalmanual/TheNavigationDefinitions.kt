package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.getTitleResByURL

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(getTitleResByURL("Psychology")).isEqualTo(R.string.psychology)
        assertThat(getTitleResByURL("Medicine")).isEqualTo(R.string.basic_medicine)
        assertThat(getTitleResByURL("Medicine#foo")).isEqualTo(R.string.basic_medicine)
    }
}